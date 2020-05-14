package org.gluu.oxd.server.persistence.service;

import org.gluu.oxd.common.ExpiredObject;
import org.gluu.oxd.common.ExpiredObjectType;
import org.gluu.oxd.common.Jackson2;
import org.gluu.oxd.common.PersistenceConfigKeys;
import org.gluu.oxd.server.OxdServerConfiguration;
import org.gluu.oxd.server.persistence.modal.OrganizationBranch;
import org.gluu.oxd.server.persistence.modal.RpObject;
import org.gluu.oxd.server.persistence.providers.GluuPersistenceConfiguration;
import org.gluu.oxd.server.persistence.providers.PersistenceEntryManagerFactory;
import org.gluu.oxd.server.service.MigrationService;
import org.gluu.oxd.server.service.Rp;
import org.gluu.persist.PersistenceEntryManager;
import org.gluu.persist.exception.EntryPersistenceException;
import org.gluu.persist.model.base.SimpleBranch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public class GluuPersistenceService implements PersistenceService {

    private static final Logger LOG = LoggerFactory.getLogger(GluuPersistenceService.class);
    private OxdServerConfiguration configuration;
    private PersistenceEntryManager persistenceEntryManager;
    private String persistenceType;
    private String BASE_DN = "o=gluu";

    public GluuPersistenceService(OxdServerConfiguration configuration) {
        this.configuration = configuration;
    }

    public GluuPersistenceService(OxdServerConfiguration configuration, String persistenceType) {
        this.configuration = configuration;
        this.persistenceType = persistenceType;
    }

    public void create() {
        LOG.debug("Creating GluuPersistenceService ...");
        try {
            GluuPersistenceConfiguration gluuPersistenceConfiguration = new GluuPersistenceConfiguration(configuration);
            Properties props = gluuPersistenceConfiguration.getPersistenceProps();

            if (props.getProperty(PersistenceConfigKeys.PersistenceType.getKeyName()).equalsIgnoreCase("ldap")) {

                this.persistenceEntryManager = PersistenceEntryManagerFactory.createLdapPersistenceEntryManager(props);

            } else if (props.getProperty(PersistenceConfigKeys.PersistenceType.getKeyName()).equalsIgnoreCase("couchbase")
                    || props.getProperty(PersistenceConfigKeys.PersistenceType.getKeyName()).equalsIgnoreCase("hybrid")) {

                this.persistenceEntryManager = PersistenceEntryManagerFactory.createCouchbasePersistenceEntryManager(props);
            }

            if (this.persistenceType != null && !this.persistenceType.equalsIgnoreCase(props.getProperty(PersistenceConfigKeys.PersistenceType.getKeyName()))) {
                LOG.error("The value of the `storage` field in `oxd-server.yml` does not matches with `persistence.type` in `gluu.property` file. \n `storage` value: {} \n `persistence.type` value : {}"
                        , this.persistenceType, this.persistenceEntryManager.getPersistenceType());
                throw new RuntimeException("The value of the `storage` field in `oxd-server.yml` does not matches with `persistence.type` in `gluu.property` file. \n `storage` value: " + this.persistenceType + " \n `persistence.type` value : "
                        + this.persistenceEntryManager.getPersistenceType());
            }
            prepareBranch();
        } catch (Exception e) {
            throw new IllegalStateException("Error starting GluuPersistenceService", e);
        }
    }

    public void prepareBranch() {
        if (!this.persistenceEntryManager.hasBranchesSupport(BASE_DN)) {
            return;
        }
        //create `o=gluu` if not present
        if (!containsBranch(BASE_DN)) {
            addOrganizationBranch(BASE_DN, null);
        }
        //create `ou=configuration,o=gluu` if not present
        if (!containsBranch(String.format("%s,%s", ou("configuration"), BASE_DN))) {
            addBranch(String.format("%s,%s", ou("configuration"), BASE_DN), "configuration");
        }
        //create `ou=oxd,ou=configuration,o=gluu` if not present
        if (!containsBranch(String.format("%s,%s,%s", ou("oxd"), ou("configuration"), BASE_DN))) {
            addBranch("ou=oxd,ou=configuration,o=gluu", "oxd");
        }
        //create `ou=oxd,o=gluu` if not present
        if (!containsBranch(getOxdDn())) {
            addBranch(getOxdDn(), "oxd");
        }
        //create `ou=rp,ou=oxd,o=gluu` if not present
        if (!containsBranch(String.format("%s,%s", getRpOu(), getOxdDn()))) {
            addBranch(String.format("%s,%s", getRpOu(), getOxdDn()), "rp");
        }
        //create `ou=expiredObjects,ou=oxd,o=gluu` if not present
        if (!containsBranch(String.format("%s,%s", getExpiredObjOu(), getOxdDn()))) {
            addBranch(String.format("%s,%s", getExpiredObjOu(), getOxdDn()), "expiredObjects");
        }
    }

    public boolean containsBranch(String dn) {
        return this.persistenceEntryManager.contains(dn, SimpleBranch.class);
    }

    public void addOrganizationBranch(String dn, String oName) {
        OrganizationBranch branch = new OrganizationBranch();
        branch.setOrganizationName(oName);
        branch.setDn(dn);

        this.persistenceEntryManager.persist(branch);
    }

    public void addBranch(String dn, String ouName) {
        SimpleBranch branch = new SimpleBranch();
        branch.setOrganizationalUnitName(ouName);
        branch.setDn(dn);

        this.persistenceEntryManager.persist(branch);
    }

    public boolean create(Rp rp) {
        try {
            RpObject rpObj = new RpObject(getDnForRp(rp.getOxdId()), rp.getOxdId(), Jackson2.serializeWithoutNulls(rp));
            this.persistenceEntryManager.persist(rpObj);
            LOG.debug("RP created successfully. RP : {} ", rp);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to create RP: {} ", rp, e);
        }
        return false;
    }

    public boolean createExpiredObject(ExpiredObject obj) {
        try {
            if (isExpiredObjectPresent(obj.getKey())) {
                LOG.warn("Expired_object already present. Object : {} ", obj.getKey());
                return true;
            }
            obj.setTypeString(obj.getType().getValue());
            obj.setDn(getDnForExpiredObj(obj.getKey()));
            this.persistenceEntryManager.persist(obj);
            LOG.debug("Expired_object created successfully. Object : {} ", obj.getKey());
            return true;
        } catch (Exception e) {
            LOG.error("Failed to create ExpiredObject: {} ", obj.getKey(), e);
        }
        return false;
    }

    public boolean update(Rp rp) {
        try {
            RpObject rpObj = new RpObject(getDnForRp(rp.getOxdId()), rp.getOxdId(), Jackson2.serializeWithoutNulls(rp));
            this.persistenceEntryManager.merge(rpObj);
            LOG.debug("RP updated successfully. RP : {} ", rpObj);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to update RP: {} ", rp, e);
        }
        return false;
    }

    public Rp getRp(String oxdId) {
        try {
            RpObject rpFromGluuPersistance = getRpObject(oxdId, new String[0]);

            Rp rp = MigrationService.parseRp(rpFromGluuPersistance.getData());
            if (rp != null) {
                LOG.debug("Found RP id: {}, RP : {} ", oxdId, rp);
                return rp;
            }
            LOG.error("Failed to fetch RP by id: {} ", oxdId);
            return null;
        } catch (Exception e) {
            LOG.error("Failed to update oxdId: {} ", oxdId, e);
        }
        return null;
    }

    private RpObject getRpObject(String oxdId, String... returnAttributes) {
        return (RpObject) this.persistenceEntryManager.find(getDnForRp(oxdId), RpObject.class, returnAttributes);
    }

    public ExpiredObject getExpiredObject(String key) {
        try {
            ExpiredObject expiredObject = getExpiredObject(key, null);
            expiredObject.setType(ExpiredObjectType.fromValue(expiredObject.getTypeString()));
            if (expiredObject != null) {
                LOG.debug("Found ExpiredObject id: {} , ExpiredObject : {} ", key, expiredObject);
                return expiredObject;
            }
            LOG.error("Failed to fetch ExpiredObject by id: {} ", key);
            return null;
        } catch (Exception e) {
            if (((e instanceof EntryPersistenceException)) && (e.getMessage().contains("Failed to find entry"))) {
                LOG.warn("Failed to fetch ExpiredObject by id: {}. {} ", key, e.getMessage());
                return null;
            }
            LOG.error("Failed to fetch ExpiredObject by id: {} ", key, e);
        }
        return null;
    }

    private ExpiredObject getExpiredObject(String key, String... returnAttributes) {
        return (ExpiredObject) this.persistenceEntryManager.find(getDnForExpiredObj(key), ExpiredObject.class, returnAttributes);
    }

    public boolean isExpiredObjectPresent(String key) {
        return getExpiredObject(key) != null;
    }

    public boolean removeAllRps() {
        try {
            List<RpObject> rps = this.persistenceEntryManager.findEntries(String.format("%s,%s", new Object[]{getRpOu(), getOxdDn()}), RpObject.class, null);
            for (RpObject rp : rps) {
                this.persistenceEntryManager.remove(rp);
            }
            LOG.debug("Removed all Rps successfully. ");
            return true;
        } catch (Exception e) {
            if (((e instanceof EntryPersistenceException)) && (e.getMessage().contains("Failed to find entries"))) {
                LOG.warn("Failed to fetch RpObjects. {} ", e.getMessage());
                return true;
            }
            LOG.error("Failed to remove all Rps", e);
        }
        return false;
    }

    public Set<Rp> getRps() {
        try {
            List<RpObject> rpObjects = this.persistenceEntryManager.findEntries(String.format("%s,%s", new Object[]{getRpOu(), getOxdDn()}), RpObject.class, null);

            Set<Rp> result = new HashSet();
            for (RpObject ele : rpObjects) {
                Rp rp = MigrationService.parseRp(ele.getData());
                if (rp != null) {
                    result.add(rp);
                } else {
                    LOG.error("Failed to parse rp, id: {}, dn: {} ", ele.getId(), ele.getDn());
                }
            }
            return result;
        } catch (Exception e) {
            if (((e instanceof EntryPersistenceException)) && (e.getMessage().contains("Failed to find entries"))) {
                LOG.warn("Failed to fetch RpObjects. {} ", e.getMessage());
                return null;
            }
            LOG.error("Failed to fetch rps. Error: {} ", e.getMessage(), e);
        }
        return null;
    }

    public void destroy() {
        this.persistenceEntryManager.destroy();
    }

    public boolean remove(String oxdId) {
        try {
            RpObject rpFromPersistance = getRpObject(oxdId, new String[0]);
            this.persistenceEntryManager.remove(rpFromPersistance);
            LOG.debug("Removed rp successfully. oxdId: {} ", oxdId);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to remove rp with oxdId: {} ", oxdId, e);
        }
        return false;
    }

    public boolean deleteExpiredObjectsByKey(String key) {
        try {
            ExpiredObject expiredObject = getExpiredObject(key);
            this.persistenceEntryManager.remove(expiredObject);
            LOG.debug("Removed expired_objects successfully: {} ", key);
            return true;
        } catch (Exception e) {
            LOG.error("Failed to remove expired_objects: {} ", key, e);
        }
        return false;
    }

    public boolean deleteAllExpiredObjects() {
        try {
            List<ExpiredObject> expiredObjects = this.persistenceEntryManager.findEntries(String.format("%s,%s", new Object[]{getExpiredObjOu(), getOxdDn()}), ExpiredObject.class, null);
            for (ExpiredObject ele : expiredObjects) {
                this.persistenceEntryManager.remove(ele);
            }
            LOG.debug("Removed all expired_objects successfully. ");
            return true;
        } catch (Exception e) {
            LOG.error("Failed to remove expired_objects. ", e);
        }
        return false;
    }

    public String getDnForRp(String oxdId) {
        return String.format("id=%s,%s,%s", new Object[]{oxdId, getRpOu(), getOxdDn()});
    }

    public String getDnForExpiredObj(String oxdId) {
        return String.format("key=%s,%s,%s", new Object[]{oxdId, getExpiredObjOu(), getOxdDn()});
    }

    public String ou(String ouName) {
        return String.format("ou=%s", ouName);
    }

    private String getOxdDn() {
        return String.format("%s,%s", ou("oxd"), BASE_DN);
    }

    private String getRpOu() {
        return ou("rp");
    }

    private String getExpiredObjOu() {
        return ou("expiredObjects");
    }
}
