package org.xdi.oxauth.model.configuration;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * FIDO 2 configuration
 *
 * @author Yuriy Movchan Date: 11/05/2018
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Fido2Configuration {

    private String certFilesFolder;

    private String mdsTocRootCertFile;
    private String mdsTocFilesFolder;

    private String certificationServerMetadataFolder;

    private Boolean disable;

    public String getCertificationServerMetadataFolder() {
        return certificationServerMetadataFolder;
    }

    public void setCertificationServerMetadataFolder(String certificationServerMetadataFolder) {
        this.certificationServerMetadataFolder = certificationServerMetadataFolder;
    }

    public String getCertFilesFolder() {
        return certFilesFolder;
    }

    public void setCertFilesFolder(String certFilesFolder) {
        this.certFilesFolder = certFilesFolder;
    }

    public String getMdsTocRootCertFile() {
        return mdsTocRootCertFile;
    }

    public void setMdsTocRootCertFile(String mdsTocRootCertFile) {
        this.mdsTocRootCertFile = mdsTocRootCertFile;
    }

    public String getMdsTocFilesFolder() {
        return mdsTocFilesFolder;
    }

    public void setMdsTocFilesFolder(String mdsTocFilesFolder) {
        this.mdsTocFilesFolder = mdsTocFilesFolder;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

}
