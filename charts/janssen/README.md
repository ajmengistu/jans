# janssen

![Version: 1.0.0-beta.16](https://img.shields.io/badge/Version-1.0.0--beta.16-informational?style=flat-square) ![AppVersion: 1.0.0](https://img.shields.io/badge/AppVersion-1.0.0-informational?style=flat-square)

Janssen Access and Identity Management

**Homepage:** <https://jans.io>

## Maintainers

| Name | Email | Url |
| ---- | ------ | --- |
| moabu | support@jans.io |  |

## Source Code

* <https://jans.io>
* <https://github.com/JanssenProject/jans/charts/janssen>

## Requirements

Kubernetes: `>=v1.21.0-0`

| Repository | Name | Version |
|------------|------|---------|
|  | auth-server | 1.0.0-beta.16 |
|  | auth-server-key-rotation | 1.0.0-beta.16 |
|  | client-api | 1.0.0-beta.16 |
|  | config | 1.0.0-beta.16 |
|  | config-api | 1.0.0-beta.16 |
|  | fido2 | 1.0.0-beta.16 |
|  | nginx-ingress | 1.0.0-beta.16 |
|  | opendj | 1.0.0-beta.16 |
|  | persistence | 1.0.0-beta.16 |
|  | scim | 1.0.0-beta.16 |

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| auth-server | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/auth-server","tag":"1.0.0-beta.16"},"livenessProbe":{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"readinessProbe":{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"2500m","memory":"2500Mi"},"requests":{"cpu":"2500m","memory":"2500Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | OAuth Authorization Server, the OpenID Connect Provider, the UMA Authorization Server--this is the main Internet facing component of Janssen. It's the service that returns tokens, JWT's and identity assertions. This service must be Internet facing. |
| auth-server-key-rotation | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/certmanager","tag":"1.0.0-beta.16"},"keysLife":48,"resources":{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | Responsible for regenerating auth-keys per x hours |
| auth-server-key-rotation.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| auth-server-key-rotation.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| auth-server-key-rotation.dnsConfig | object | `{}` | Add custom dns config |
| auth-server-key-rotation.dnsPolicy | string | `""` | Add custom dns policy |
| auth-server-key-rotation.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| auth-server-key-rotation.image.pullSecrets | list | `[]` | Image Pull Secrets |
| auth-server-key-rotation.image.repository | string | `"janssenproject/certmanager"` | Image  to use for deploying. |
| auth-server-key-rotation.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| auth-server-key-rotation.keysLife | int | `48` | Auth server key rotation keys life in hours |
| auth-server-key-rotation.resources | object | `{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}}` | Resource specs. |
| auth-server-key-rotation.resources.limits.cpu | string | `"300m"` | CPU limit. |
| auth-server-key-rotation.resources.limits.memory | string | `"300Mi"` | Memory limit. |
| auth-server-key-rotation.resources.requests.cpu | string | `"300m"` | CPU request. |
| auth-server-key-rotation.resources.requests.memory | string | `"300Mi"` | Memory request. |
| auth-server-key-rotation.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| auth-server-key-rotation.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| auth-server-key-rotation.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| auth-server-key-rotation.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| auth-server-key-rotation.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| auth-server.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| auth-server.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| auth-server.dnsConfig | object | `{}` | Add custom dns config |
| auth-server.dnsPolicy | string | `""` | Add custom dns policy |
| auth-server.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| auth-server.hpa.behavior | object | `{}` | Scaling Policies |
| auth-server.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| auth-server.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| auth-server.image.pullSecrets | list | `[]` | Image Pull Secrets |
| auth-server.image.repository | string | `"janssenproject/auth-server"` | Image  to use for deploying. |
| auth-server.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| auth-server.livenessProbe | object | `{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the liveness healthcheck for the auth server if needed. |
| auth-server.livenessProbe.exec | object | `{"command":["python3","/app/scripts/healthcheck.py"]}` | Executes the python3 healthcheck. https://github.com/JanssenProject/docker-jans-auth-server/blob/master/scripts/healthcheck.py |
| auth-server.readinessProbe | object | `{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5}` | Configure the readiness healthcheck for the auth server if needed. https://github.com/JanssenProject/docker-jans-auth-server/blob/master/scripts/healthcheck.py |
| auth-server.replicas | int | `1` | Service replica number. |
| auth-server.resources | object | `{"limits":{"cpu":"2500m","memory":"2500Mi"},"requests":{"cpu":"2500m","memory":"2500Mi"}}` | Resource specs. |
| auth-server.resources.limits.cpu | string | `"2500m"` | CPU limit. |
| auth-server.resources.limits.memory | string | `"2500Mi"` | Memory limit. |
| auth-server.resources.requests.cpu | string | `"2500m"` | CPU request. |
| auth-server.resources.requests.memory | string | `"2500Mi"` | Memory request. |
| auth-server.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| auth-server.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| auth-server.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| auth-server.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| auth-server.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| client-api | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/client-api","tag":"1.0.0-beta.16"},"livenessProbe":{"exec":{"command":["curl","-k","https://localhost:8443/health-check"]},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"readinessProbe":{"initialDelaySeconds":60,"periodSeconds":25,"tcpSocket":{"port":8443},"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"1000m","memory":"400Mi"},"requests":{"cpu":"1000m","memory":"400Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | Middleware API to help application developers call an OAuth, OpenID or UMA server. You may wonder why this is necessary. It makes it easier for client developers to use OpenID signing and encryption features, without becoming crypto experts. This API provides some high level endpoints to do some of the heavy lifting. |
| client-api.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| client-api.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| client-api.dnsConfig | object | `{}` | Add custom dns config |
| client-api.dnsPolicy | string | `""` | Add custom dns policy |
| client-api.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| client-api.hpa.behavior | object | `{}` | Scaling Policies |
| client-api.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| client-api.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| client-api.image.pullSecrets | list | `[]` | Image Pull Secrets |
| client-api.image.repository | string | `"janssenproject/client-api"` | Image  to use for deploying. |
| client-api.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| client-api.livenessProbe | object | `{"exec":{"command":["curl","-k","https://localhost:8443/health-check"]},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the liveness healthcheck for the auth server if needed. |
| client-api.livenessProbe.exec | object | `{"command":["curl","-k","https://localhost:8443/health-check"]}` | Executes the python3 healthcheck. |
| client-api.readinessProbe | object | `{"initialDelaySeconds":60,"periodSeconds":25,"tcpSocket":{"port":8443},"timeoutSeconds":5}` | Configure the readiness healthcheck for the auth server if needed. |
| client-api.replicas | int | `1` | Service replica number. |
| client-api.resources | object | `{"limits":{"cpu":"1000m","memory":"400Mi"},"requests":{"cpu":"1000m","memory":"400Mi"}}` | Resource specs. |
| client-api.resources.limits.cpu | string | `"1000m"` | CPU limit. |
| client-api.resources.limits.memory | string | `"400Mi"` | Memory limit. |
| client-api.resources.requests.cpu | string | `"1000m"` | CPU request. |
| client-api.resources.requests.memory | string | `"400Mi"` | Memory request. |
| client-api.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| client-api.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| client-api.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| client-api.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| client-api.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| config | object | `{"additionalAnnotations":{},"additionalLabels":{},"adminPassword":"Test1234#","city":"Austin","configmap":{"cnCacheType":"NATIVE_PERSISTENCE","cnClientApiAdminCertCn":"client-api","cnClientApiApplicationCertCn":"client-api","cnClientApiBindIpAddresses":"*","cnConfigGoogleSecretNamePrefix":"janssen","cnConfigGoogleSecretVersionId":"latest","cnConfigKubernetesConfigMap":"cn","cnCouchbaseBucketPrefix":"jans","cnCouchbaseCrt":"SWFtTm90YVNlcnZpY2VBY2NvdW50Q2hhbmdlTWV0b09uZQo=","cnCouchbaseIndexNumReplica":0,"cnCouchbasePassword":"P@ssw0rd","cnCouchbaseSuperUser":"admin","cnCouchbaseSuperUserPassword":"Test1234#","cnCouchbaseUrl":"cbjanssen.default.svc.cluster.local","cnCouchbaseUser":"janssen","cnGoogleProjectId":"google-project-to-save-config-and-secrets-to","cnGoogleSecretManagerPassPhrase":"Test1234#","cnGoogleSecretManagerServiceAccount":"SWFtTm90YVNlcnZpY2VBY2NvdW50Q2hhbmdlTWV0b09uZQo=","cnGoogleSpannerDatabaseId":"","cnGoogleSpannerInstanceId":"","cnJettyRequestHeaderSize":8192,"cnLdapUrl":"opendj:1636","cnMaxRamPercent":"75.0","cnPersistenceLdapMapping":"default","cnRedisSentinelGroup":"","cnRedisSslTruststore":"","cnRedisType":"STANDALONE","cnRedisUrl":"redis.redis.svc.cluster.local:6379","cnRedisUseSsl":false,"cnScimProtectionMode":"OAUTH","cnSecretGoogleSecretNamePrefix":"janssen","cnSecretGoogleSecretVersionId":"latest","cnSecretKubernetesSecret":"cn","cnSqlDbDialect":"mysql","cnSqlDbHost":"my-release-mysql.default.svc.cluster.local","cnSqlDbName":"jans","cnSqlDbPort":3306,"cnSqlDbTimezone":"UTC","cnSqlDbUser":"jans","cnSqldbUserPassword":"Test1234#","lbAddr":""},"countryCode":"US","dnsConfig":{},"dnsPolicy":"","email":"support@jans.io","image":{"pullSecrets":[],"repository":"janssenproject/configurator","tag":"1.0.0-beta.16"},"ldapPassword":"P@ssw0rds","orgName":"Janssen","redisPassword":"P@assw0rd","resources":{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}},"state":"TX","usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | Configuration parameters for setup and initial configuration secret and config layers used by Janssen services. |
| config-api | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/config-api","tag":"1.0.0-beta.16"},"livenessProbe":{"httpGet":{"path":"/jans-config-api/api/v1/health/live","port":8074},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"readinessProbe":{"httpGet":{"path":"jans-config-api/api/v1/health/ready","port":8074},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"1000m","memory":"400Mi"},"requests":{"cpu":"1000m","memory":"400Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | Config Api endpoints can be used to configure the auth-server, which is an open-source OpenID Connect Provider (OP) and UMA Authorization Server (AS). |
| config-api.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| config-api.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| config-api.dnsConfig | object | `{}` | Add custom dns config |
| config-api.dnsPolicy | string | `""` | Add custom dns policy |
| config-api.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| config-api.hpa.behavior | object | `{}` | Scaling Policies |
| config-api.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| config-api.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| config-api.image.pullSecrets | list | `[]` | Image Pull Secrets |
| config-api.image.repository | string | `"janssenproject/config-api"` | Image  to use for deploying. |
| config-api.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| config-api.livenessProbe | object | `{"httpGet":{"path":"/jans-config-api/api/v1/health/live","port":8074},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the liveness healthcheck for the auth server if needed. |
| config-api.livenessProbe.httpGet | object | `{"path":"/jans-config-api/api/v1/health/live","port":8074}` | http liveness probe endpoint |
| config-api.readinessProbe.httpGet | object | `{"path":"jans-config-api/api/v1/health/ready","port":8074}` | http readiness probe endpoint |
| config-api.replicas | int | `1` | Service replica number. |
| config-api.resources | object | `{"limits":{"cpu":"1000m","memory":"400Mi"},"requests":{"cpu":"1000m","memory":"400Mi"}}` | Resource specs. |
| config-api.resources.limits.cpu | string | `"1000m"` | CPU limit. |
| config-api.resources.limits.memory | string | `"400Mi"` | Memory limit. |
| config-api.resources.requests.cpu | string | `"1000m"` | CPU request. |
| config-api.resources.requests.memory | string | `"400Mi"` | Memory request. |
| config-api.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| config-api.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| config-api.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| config-api.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| config-api.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| config.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| config.adminPassword | string | `"Test1234#"` | Admin password to log in to the UI. |
| config.city | string | `"Austin"` | City. Used for certificate creation. |
| config.configmap.cnCacheType | string | `"NATIVE_PERSISTENCE"` | Cache type. `NATIVE_PERSISTENCE`, `REDIS`. or `IN_MEMORY`. Defaults to `NATIVE_PERSISTENCE` . |
| config.configmap.cnClientApiAdminCertCn | string | `"client-api"` | Client-api OAuth client admin certificate common name. This should be left to the default value client-api . |
| config.configmap.cnClientApiApplicationCertCn | string | `"client-api"` | Client-api OAuth client application certificate common name. This should be left to the default value client-api. |
| config.configmap.cnClientApiBindIpAddresses | string | `"*"` | Client-api bind address. This limits what ip ranges can access the client-api. This should be left as * and controlled by a NetworkPolicy |
| config.configmap.cnConfigGoogleSecretNamePrefix | string | `"janssen"` | Prefix for Janssen configuration secret in Google Secret Manager. Defaults to janssen. If left intact janssen-configuration secret will be created. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnConfigGoogleSecretVersionId | string | `"latest"` | Secret version to be used for configuration. Defaults to latest and should normally always stay that way. Used only when global.configAdapterName and global.configSecretAdapter is set to google. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnConfigKubernetesConfigMap | string | `"cn"` | The name of the Kubernetes ConfigMap that will hold the configuration layer |
| config.configmap.cnCouchbaseBucketPrefix | string | `"jans"` | The prefix of couchbase buckets. This helps with separation in between different environments and allows for the same couchbase cluster to be used by different setups of Janssen. |
| config.configmap.cnCouchbaseCrt | string | `"SWFtTm90YVNlcnZpY2VBY2NvdW50Q2hhbmdlTWV0b09uZQo="` | Couchbase certificate authority string. This must be encoded using base64. This can also be found in your couchbase UI Security > Root Certificate. In mTLS setups this is not required. |
| config.configmap.cnCouchbaseIndexNumReplica | int | `0` | The number of replicas per index created. Please note that the number of index nodes must be one greater than the number of index replicas. That means if your couchbase cluster only has 2 index nodes you cannot place the number of replicas to be higher than 1. |
| config.configmap.cnCouchbasePassword | string | `"P@ssw0rd"` | Couchbase password for the restricted user config.configmap.cnCouchbaseUser  that is often used inside the services. The password must contain one digit, one uppercase letter, one lower case letter and one symbol . |
| config.configmap.cnCouchbaseSuperUser | string | `"admin"` | The Couchbase super user (admin) user name. This user is used during initialization only. |
| config.configmap.cnCouchbaseSuperUserPassword | string | `"Test1234#"` | Couchbase password for the super user config.configmap.cnCouchbaseSuperUser  that is used during the initialization process. The password must contain one digit, one uppercase letter, one lower case letter and one symbol |
| config.configmap.cnCouchbaseUrl | string | `"cbjanssen.default.svc.cluster.local"` | Couchbase URL. Used only when global.cnPersistenceType is hybrid or couchbase. This should be in FQDN format for either remote or local Couchbase clusters. The address can be an internal address inside the kubernetes cluster |
| config.configmap.cnCouchbaseUser | string | `"janssen"` | Couchbase restricted user. Used only when global.cnPersistenceType is hybrid or couchbase. |
| config.configmap.cnGoogleProjectId | string | `"google-project-to-save-config-and-secrets-to"` | Project id of the google project the secret manager belongs to. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnGoogleSecretManagerPassPhrase | string | `"Test1234#"` | Passphrase for Janssen secret in Google Secret Manager. This is used for encrypting and decrypting data from the Google Secret Manager. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnGoogleSecretManagerServiceAccount | string | `"SWFtTm90YVNlcnZpY2VBY2NvdW50Q2hhbmdlTWV0b09uZQo="` | Service account with roles roles/secretmanager.admin base64 encoded string. This is used often inside the services to reach the configuration layer. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnGoogleSpannerDatabaseId | string | `""` | Google Spanner Database ID. Used only when global.cnPersistenceType is spanner. |
| config.configmap.cnGoogleSpannerInstanceId | string | `""` | Google Spanner ID. Used only when global.cnPersistenceType is spanner. |
| config.configmap.cnJettyRequestHeaderSize | int | `8192` | Jetty header size in bytes in the auth server |
| config.configmap.cnLdapUrl | string | `"opendj:1636"` | OpenDJ internal address. Leave as default. Used when `global.cnPersistenceType` is set to `ldap`. |
| config.configmap.cnMaxRamPercent | string | `"75.0"` | Value passed to Java option -XX:MaxRAMPercentage |
| config.configmap.cnPersistenceLdapMapping | string | `"default"` | Specify data that should be saved in LDAP (one of default, user, cache, site, token, or session; default to default). Note this environment only takes effect when `global.cnPersistenceType`  is set to `hybrid`. |
| config.configmap.cnRedisSentinelGroup | string | `""` | Redis Sentinel Group. Often set when `config.configmap.cnRedisType` is set to `SENTINEL`. Can be used when  `config.configmap.cnCacheType` is set to `REDIS`. |
| config.configmap.cnRedisSslTruststore | string | `""` | Redis SSL truststore. Optional. Can be used when  `config.configmap.cnCacheType` is set to `REDIS`. |
| config.configmap.cnRedisType | string | `"STANDALONE"` | Redis service type. `STANDALONE` or `CLUSTER`. Can be used when  `config.configmap.cnCacheType` is set to `REDIS`. |
| config.configmap.cnRedisUrl | string | `"redis.redis.svc.cluster.local:6379"` | Redis URL and port number <url>:<port>. Can be used when  `config.configmap.cnCacheType` is set to `REDIS`. |
| config.configmap.cnRedisUseSsl | bool | `false` | Boolean to use SSL in Redis. Can be used when  `config.configmap.cnCacheType` is set to `REDIS`. |
| config.configmap.cnScimProtectionMode | string | `"OAUTH"` | SCIM protection mode OAUTH|TEST|UMA |
| config.configmap.cnSecretGoogleSecretNamePrefix | string | `"janssen"` | Prefix for Janssen secret in Google Secret Manager. Defaults to janssen. If left janssen-secret secret will be created. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnSecretGoogleSecretVersionId | string | `"latest"` | Secret version to be used for secret configuration. Defaults to latest and should normally always stay that way. Used only when global.configAdapterName and global.configSecretAdapter is set to google. |
| config.configmap.cnSecretKubernetesSecret | string | `"cn"` | Kubernetes secret name holding configuration keys. Used when global.configSecretAdapter is set to kubernetes which is the default. |
| config.configmap.cnSqlDbDialect | string | `"mysql"` | SQL database dialect. `mysql` or `pgsql` |
| config.configmap.cnSqlDbHost | string | `"my-release-mysql.default.svc.cluster.local"` | SQL database host uri. |
| config.configmap.cnSqlDbName | string | `"jans"` | SQL database name. |
| config.configmap.cnSqlDbPort | int | `3306` | SQL database port. |
| config.configmap.cnSqlDbTimezone | string | `"UTC"` | SQL database timezone. |
| config.configmap.cnSqlDbUser | string | `"jans"` | SQL database username. |
| config.configmap.cnSqldbUserPassword | string | `"Test1234#"` | SQL password  injected the secrets . |
| config.configmap.lbAddr | string | `""` | Loadbalancer address for AWS if the FQDN is not registered. |
| config.countryCode | string | `"US"` | Country code. Used for certificate creation. |
| config.dnsConfig | object | `{}` | Add custom dns config |
| config.dnsPolicy | string | `""` | Add custom dns policy |
| config.email | string | `"support@jans.io"` | Email address of the administrator usually. Used for certificate creation. |
| config.image.pullSecrets | list | `[]` | Image Pull Secrets |
| config.image.repository | string | `"janssenproject/configurator"` | Image  to use for deploying. |
| config.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| config.ldapPassword | string | `"P@ssw0rds"` | LDAP admin password if OpennDJ is used for persistence. |
| config.orgName | string | `"Janssen"` | Organization name. Used for certificate creation. |
| config.redisPassword | string | `"P@assw0rd"` | Redis admin password if `config.configmap.cnCacheType` is set to `REDIS`. |
| config.resources | object | `{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}}` | Resource specs. |
| config.resources.limits.cpu | string | `"300m"` | CPU limit. |
| config.resources.limits.memory | string | `"300Mi"` | Memory limit. |
| config.resources.requests.cpu | string | `"300m"` | CPU request. |
| config.resources.requests.memory | string | `"300Mi"` | Memory request. |
| config.state | string | `"TX"` | State code. Used for certificate creation. |
| config.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service. |
| config.usrEnvs.normal | object | `{}` | Add custom normal envs to the service. variable1: value1 |
| config.usrEnvs.secret | object | `{}` | Add custom secret envs to the service. variable1: value1 |
| config.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| config.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| fido2 | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/fido2","tag":"1.0.0-beta.16"},"livenessProbe":{"httpGet":{"path":"/jans-fido2/sys/health-check","port":"http-fido2"},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5},"readinessProbe":{"httpGet":{"path":"/jans-fido2/sys/health-check","port":"http-fido2"},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"500m","memory":"500Mi"},"requests":{"cpu":"500m","memory":"500Mi"}},"service":{"name":"http-fido2","port":8080},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | FIDO 2.0 (FIDO2) is an open authentication standard that enables leveraging common devices to authenticate to online services in both mobile and desktop environments. |
| fido2.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| fido2.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| fido2.dnsConfig | object | `{}` | Add custom dns config |
| fido2.dnsPolicy | string | `""` | Add custom dns policy |
| fido2.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| fido2.hpa.behavior | object | `{}` | Scaling Policies |
| fido2.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| fido2.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| fido2.image.pullSecrets | list | `[]` | Image Pull Secrets |
| fido2.image.repository | string | `"janssenproject/fido2"` | Image  to use for deploying. |
| fido2.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| fido2.livenessProbe | object | `{"httpGet":{"path":"/jans-fido2/sys/health-check","port":"http-fido2"},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5}` | Configure the liveness healthcheck for the fido2 if needed. |
| fido2.livenessProbe.httpGet | object | `{"path":"/jans-fido2/sys/health-check","port":"http-fido2"}` | http liveness probe endpoint |
| fido2.readinessProbe | object | `{"httpGet":{"path":"/jans-fido2/sys/health-check","port":"http-fido2"},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the readiness healthcheck for the fido2 if needed. |
| fido2.replicas | int | `1` | Service replica number. |
| fido2.resources | object | `{"limits":{"cpu":"500m","memory":"500Mi"},"requests":{"cpu":"500m","memory":"500Mi"}}` | Resource specs. |
| fido2.resources.limits.cpu | string | `"500m"` | CPU limit. |
| fido2.resources.limits.memory | string | `"500Mi"` | Memory limit. |
| fido2.resources.requests.cpu | string | `"500m"` | CPU request. |
| fido2.resources.requests.memory | string | `"500Mi"` | Memory request. |
| fido2.service.name | string | `"http-fido2"` | The name of the fido2 port within the fido2 service. Please keep it as default. |
| fido2.service.port | int | `8080` | Port of the fido2 service. Please keep it as default. |
| fido2.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| fido2.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| fido2.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| fido2.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| fido2.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| global | object | `{"alb":{"ingress":false},"auth-server":{"appLoggers":{"auditStatsLogLevel":"INFO","auditStatsLogTarget":"FILE","authLogLevel":"INFO","authLogTarget":"STDOUT","httpLogLevel":"INFO","httpLogTarget":"FILE","ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scriptLogLevel":"INFO","scriptLogTarget":"FILE"},"authEncKeys":"RSA1_5 RSA-OAEP","authServerServiceName":"auth-server","authSigKeys":"RS256 RS384 RS512 ES256 ES384 ES512 PS256 PS384 PS512","enabled":true},"auth-server-key-rotation":{"enabled":false},"awsStorageType":"io1","azureStorageAccountType":"Standard_LRS","azureStorageKind":"Managed","client-api":{"appLoggers":{"clientApiLogLevel":"INFO","clientApiLogTarget":"STDOUT"},"clientApiServerServiceName":"client-api","enabled":false},"cloud":{"testEnviroment":false},"cnDocumentStoreType":"LOCAL","cnGoogleApplicationCredentials":"/etc/jans/conf/google-credentials.json","cnPersistenceType":"sql","config":{"enabled":true},"config-api":{"appLoggers":{"configApiLogLevel":"INFO","configApiLogTarget":"STDOUT","ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scriptLogLevel":"INFO","scriptLogTarget":"FILE"},"configApiServerServiceName":"config-api","enabled":true},"configAdapterName":"kubernetes","configSecretAdapter":"kubernetes","fido2":{"appLoggers":{"fido2LogLevel":"INFO","fido2LogTarget":"STDOUT","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE"},"enabled":true,"fido2ServiceName":"fido2"},"fqdn":"demoexample.jans.io","gcePdStorageType":"pd-standard","isFqdnRegistered":false,"istio":{"additionalAnnotations":{},"additionalLabels":{},"enabled":false,"namespace":"istio-system"},"lbIp":"22.22.22.22","nginx-ingress":{"enabled":true},"opendj":{"enabled":false,"ldapServiceName":"opendj"},"persistence":{"enabled":true},"scim":{"appLoggers":{"ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scimLogLevel":"INFO","scimLogTarget":"STDOUT","scriptLogLevel":"INFO","scriptLogTarget":"FILE"},"enabled":true,"scimServiceName":"scim"},"storageClass":{"allowVolumeExpansion":true,"allowedTopologies":[],"mountOptions":["debug"],"parameters":{},"provisioner":"microk8s.io/hostpath","reclaimPolicy":"Retain","volumeBindingMode":"WaitForFirstConsumer"},"upgrade":{"enabled":false},"usrEnvs":{"normal":{},"secret":{}}}` | Parameters used globally across all services helm charts. |
| global.alb.ingress | bool | `false` | Activates ALB ingress |
| global.auth-server-key-rotation.enabled | bool | `false` | Boolean flag to enable/disable the auth-server-key rotation cronjob chart. |
| global.auth-server.appLoggers | object | `{"auditStatsLogLevel":"INFO","auditStatsLogTarget":"FILE","authLogLevel":"INFO","authLogTarget":"STDOUT","httpLogLevel":"INFO","httpLogTarget":"FILE","ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scriptLogLevel":"INFO","scriptLogTarget":"FILE"}` | App loggers can be configured to define where the logs will be redirected to and the level of each in which it should be displayed. |
| global.auth-server.appLoggers.auditStatsLogLevel | string | `"INFO"` | jans-auth_audit.log level |
| global.auth-server.appLoggers.auditStatsLogTarget | string | `"FILE"` | jans-auth_script.log target |
| global.auth-server.appLoggers.authLogLevel | string | `"INFO"` | jans-auth.log level |
| global.auth-server.appLoggers.authLogTarget | string | `"STDOUT"` | jans-auth.log target |
| global.auth-server.appLoggers.httpLogLevel | string | `"INFO"` | http_request_response.log level |
| global.auth-server.appLoggers.httpLogTarget | string | `"FILE"` | http_request_response.log target |
| global.auth-server.appLoggers.ldapStatsLogLevel | string | `"INFO"` | jans-auth_persistence_ldap_statistics.log level |
| global.auth-server.appLoggers.ldapStatsLogTarget | string | `"FILE"` | jans-auth_persistence_ldap_statistics.log target |
| global.auth-server.appLoggers.persistenceDurationLogLevel | string | `"INFO"` | jans-auth_persistence_duration.log level |
| global.auth-server.appLoggers.persistenceDurationLogTarget | string | `"FILE"` | jans-auth_persistence_duration.log target |
| global.auth-server.appLoggers.persistenceLogLevel | string | `"INFO"` | jans-auth_persistence.log level |
| global.auth-server.appLoggers.persistenceLogTarget | string | `"FILE"` | jans-auth_persistence.log target |
| global.auth-server.appLoggers.scriptLogLevel | string | `"INFO"` | jans-auth_script.log level |
| global.auth-server.appLoggers.scriptLogTarget | string | `"FILE"` | jans-auth_script.log target |
| global.auth-server.authEncKeys | string | `"RSA1_5 RSA-OAEP"` | space-separated key algorithm for encryption (default to `RSA1_5 RSA-OAEP`) |
| global.auth-server.authServerServiceName | string | `"auth-server"` | Name of the auth-server service. Please keep it as default. |
| global.auth-server.authSigKeys | string | `"RS256 RS384 RS512 ES256 ES384 ES512 PS256 PS384 PS512"` | space-separated key algorithm for signing (default to `RS256 RS384 RS512 ES256 ES384 ES512 PS256 PS384 PS512`) |
| global.auth-server.enabled | bool | `true` | Boolean flag to enable/disable auth-server chart. You should never set this to false. |
| global.awsStorageType | string | `"io1"` | Volume storage type if using AWS volumes. |
| global.azureStorageAccountType | string | `"Standard_LRS"` | Volume storage type if using Azure disks. |
| global.azureStorageKind | string | `"Managed"` | Azure storage kind if using Azure disks |
| global.client-api.appLoggers | object | `{"clientApiLogLevel":"INFO","clientApiLogTarget":"STDOUT"}` | App loggers can be configured to define where the logs will be redirected to and the level of each in which it should be displayed. |
| global.client-api.appLoggers.clientApiLogLevel | string | `"INFO"` | client-api.log level |
| global.client-api.appLoggers.clientApiLogTarget | string | `"STDOUT"` | client-api.log target |
| global.client-api.clientApiServerServiceName | string | `"client-api"` | Name of the client-api service. Please keep it as default. |
| global.client-api.enabled | bool | `false` | Boolean flag to enable/disable the client-api chart. |
| global.cloud.testEnviroment | bool | `false` | Boolean flag if enabled will strip resources requests and limits from all services. |
| global.cnDocumentStoreType | string | `"LOCAL"` | Document store type to use for shibboleth files LOCAL. |
| global.cnGoogleApplicationCredentials | string | `"/etc/jans/conf/google-credentials.json"` | Base64 encoded service account. The sa must have roles/secretmanager.admin to use Google secrets and roles/spanner.databaseUser to use Spanner. |
| global.cnPersistenceType | string | `"sql"` | Persistence backend to run Janssen with ldap|couchbase|hybrid|sql|spanner. |
| global.config | object | `{"enabled":true}` | Open banking external signing jwks uri. Used in SSA Validation. |
| global.config-api.appLoggers | object | `{"configApiLogLevel":"INFO","configApiLogTarget":"STDOUT","ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scriptLogLevel":"INFO","scriptLogTarget":"FILE"}` | App loggers can be configured to define where the logs will be redirected to and the level of each in which it should be displayed. |
| global.config-api.appLoggers.configApiLogLevel | string | `"INFO"` | configapi.log level |
| global.config-api.appLoggers.configApiLogTarget | string | `"STDOUT"` | configapi.log target |
| global.config-api.appLoggers.ldapStatsLogLevel | string | `"INFO"` | config-api_persistence_ldap_statistics.log level |
| global.config-api.appLoggers.ldapStatsLogTarget | string | `"FILE"` | config-api_persistence_ldap_statistics.log target |
| global.config-api.appLoggers.persistenceDurationLogLevel | string | `"INFO"` | config-api_persistence_duration.log level |
| global.config-api.appLoggers.persistenceDurationLogTarget | string | `"FILE"` | config-api_persistence_duration.log target |
| global.config-api.appLoggers.persistenceLogLevel | string | `"INFO"` | jans-auth_persistence.log level |
| global.config-api.appLoggers.persistenceLogTarget | string | `"FILE"` | config-api_persistence.log target |
| global.config-api.appLoggers.scriptLogLevel | string | `"INFO"` | config-api_script.log level |
| global.config-api.appLoggers.scriptLogTarget | string | `"FILE"` | config-api_script.log target |
| global.config-api.configApiServerServiceName | string | `"config-api"` | Name of the config-api service. Please keep it as default. |
| global.config-api.enabled | bool | `true` | Boolean flag to enable/disable the config-api chart. |
| global.config.enabled | bool | `true` | Boolean flag to enable/disable the configuration chart. This normally should never be false |
| global.configAdapterName | string | `"kubernetes"` | The config backend adapter that will hold Janssen configuration layer. google|kubernetes |
| global.configSecretAdapter | string | `"kubernetes"` | The config backend adapter that will hold Janssen secret layer. google|kubernetes |
| global.fido2.appLoggers | object | `{"fido2LogLevel":"INFO","fido2LogTarget":"STDOUT","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE"}` | App loggers can be configured to define where the logs will be redirected to and the level of each in which it should be displayed. |
| global.fido2.appLoggers.fido2LogLevel | string | `"INFO"` | fido2.log level |
| global.fido2.appLoggers.fido2LogTarget | string | `"STDOUT"` | fido2.log target |
| global.fido2.appLoggers.persistenceLogLevel | string | `"INFO"` | fido2_persistence.log level |
| global.fido2.appLoggers.persistenceLogTarget | string | `"FILE"` | fido2_persistence.log target |
| global.fido2.enabled | bool | `true` | Boolean flag to enable/disable the fido2 chart. |
| global.fido2.fido2ServiceName | string | `"fido2"` | Name of the fido2 service. Please keep it as default. |
| global.gcePdStorageType | string | `"pd-standard"` | GCE storage kind if using Google disks |
| global.isFqdnRegistered | bool | `false` | Boolean flag to enable mapping global.lbIp  to global.fqdn inside pods on clouds that provide static ip for loadbalancers. On cloud that provide only addresses to the LB this flag will enable a script to actively scan config.configmap.lbAddr and update the hosts file inside the pods automatically. |
| global.istio.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| global.istio.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| global.istio.enabled | bool | `false` | Boolean flag that enables using istio side cars with Janssen services. |
| global.istio.namespace | string | `"istio-system"` | Boolean flag that enables using istio gateway for Janssen. This assumes istio ingress is installed and hence the LB is available. |
| global.lbIp | string | `"22.22.22.22"` | The Loadbalancer IP created by nginx or istio on clouds that provide static IPs. This is not needed if `global.fqdn` is globally resolvable. |
| global.nginx-ingress.enabled | bool | `true` | Boolean flag to enable/disable the nginx-ingress definitions chart. |
| global.opendj.enabled | bool | `false` | Boolean flag to enable/disable the OpenDJ  chart. |
| global.opendj.ldapServiceName | string | `"opendj"` | Name of the OpenDJ service. Please keep it as default. |
| global.persistence.enabled | bool | `true` | Boolean flag to enable/disable the persistence chart. |
| global.scim.appLoggers | object | `{"ldapStatsLogLevel":"INFO","ldapStatsLogTarget":"FILE","persistenceDurationLogLevel":"INFO","persistenceDurationLogTarget":"FILE","persistenceLogLevel":"INFO","persistenceLogTarget":"FILE","scimLogLevel":"INFO","scimLogTarget":"STDOUT","scriptLogLevel":"INFO","scriptLogTarget":"FILE"}` | App loggers can be configured to define where the logs will be redirected to and the level of each in which it should be displayed. |
| global.scim.appLoggers.ldapStatsLogLevel | string | `"INFO"` | jans-scim_persistence_ldap_statistics.log level |
| global.scim.appLoggers.ldapStatsLogTarget | string | `"FILE"` | jans-scim_persistence_ldap_statistics.log target |
| global.scim.appLoggers.persistenceDurationLogLevel | string | `"INFO"` | jans-scim_persistence_duration.log level |
| global.scim.appLoggers.persistenceDurationLogTarget | string | `"FILE"` | jans-scim_persistence_duration.log target |
| global.scim.appLoggers.persistenceLogLevel | string | `"INFO"` | jans-scim_persistence.log level |
| global.scim.appLoggers.persistenceLogTarget | string | `"FILE"` | jans-scim_persistence.log target |
| global.scim.appLoggers.scimLogLevel | string | `"INFO"` | jans-scim.log level |
| global.scim.appLoggers.scimLogTarget | string | `"STDOUT"` | jans-scim.log target |
| global.scim.appLoggers.scriptLogLevel | string | `"INFO"` | jans-scim_script.log level |
| global.scim.appLoggers.scriptLogTarget | string | `"FILE"` | jans-scim_script.log target |
| global.scim.enabled | bool | `true` | Boolean flag to enable/disable the SCIM chart. |
| global.scim.scimServiceName | string | `"scim"` | Name of the scim service. Please keep it as default. |
| global.storageClass | object | `{"allowVolumeExpansion":true,"allowedTopologies":[],"mountOptions":["debug"],"parameters":{},"provisioner":"microk8s.io/hostpath","reclaimPolicy":"Retain","volumeBindingMode":"WaitForFirstConsumer"}` | StorageClass section for OpenDJ charts. This is not currently used by the openbanking distribution. You may specify custom parameters as needed. |
| global.storageClass.parameters | object | `{}` | parameters: |
| global.upgrade.enabled | bool | `false` | Boolean flag used when running upgrading through versions command. Used when upgrading with LDAP as the persistence to load the 101x ldif. |
| global.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service. Envs defined in global.userEnvs will be globally available to all services |
| global.usrEnvs.normal | object | `{}` | Add custom normal envs to the service. variable1: value1 |
| global.usrEnvs.secret | object | `{}` | Add custom secret envs to the service. variable1: value1 |
| nginx-ingress | object | `{"ingress":{"additionalAnnotations":{},"additionalLabels":{},"authServerAdditionalAnnotations":{},"authServerEnabled":true,"authServerLabels":{},"configApiAdditionalAnnotations":{},"configApiEnabled":true,"configApiLabels":{},"deviceCodeAdditionalAnnotations":{},"deviceCodeEnabled":true,"deviceCodeLabels":{},"fido2ConfigAdditionalAnnotations":{},"fido2ConfigEnabled":false,"fido2ConfigLabels":{},"firebaseMessagingAdditionalAnnotations":{},"firebaseMessagingEnabled":true,"firebaseMessagingLabels":{},"hosts":["demoexample.jans.io"],"openidAdditionalAnnotations":{},"openidConfigEnabled":true,"openidConfigLabels":{},"path":"/","scimAdditionalAnnotations":{},"scimConfigAdditionalAnnotations":{},"scimConfigEnabled":false,"scimConfigLabels":{},"scimEnabled":false,"scimLabels":{},"tls":[{"hosts":["demoexample.jans.io"],"secretName":"tls-certificate"}],"u2fAdditionalAnnotations":{},"u2fConfigEnabled":true,"u2fConfigLabels":{},"uma2AdditionalAnnotations":{},"uma2ConfigEnabled":true,"uma2ConfigLabels":{},"webdiscoveryAdditionalAnnotations":{},"webdiscoveryEnabled":true,"webdiscoveryLabels":{},"webfingerAdditionalAnnotations":{},"webfingerEnabled":true,"webfingerLabels":{}}}` | Nginx ingress definitions chart |
| nginx-ingress.ingress.additionalAnnotations | object | `{}` | Additional annotations that will be added across all ingress definitions in the format of {cert-manager.io/issuer: "letsencrypt-prod"} Enable client certificate authentication nginx.ingress.kubernetes.io/auth-tls-verify-client: "optional" Create the secret containing the trusted ca certificates nginx.ingress.kubernetes.io/auth-tls-secret: "janssen/tls-certificate" Specify the verification depth in the client certificates chain nginx.ingress.kubernetes.io/auth-tls-verify-depth: "1" Specify if certificates are passed to upstream server nginx.ingress.kubernetes.io/auth-tls-pass-certificate-to-upstream: "true" |
| nginx-ingress.ingress.additionalLabels | object | `{}` | Additional labels that will be added across all ingress definitions in the format of {mylabel: "myapp"} |
| nginx-ingress.ingress.authServerAdditionalAnnotations | object | `{}` | Auth server ingress resource additional annotations. |
| nginx-ingress.ingress.authServerEnabled | bool | `true` | Enable Auth server endpoints /jans-auth |
| nginx-ingress.ingress.authServerLabels | object | `{}` | Auth server ingress resource labels. key app is taken |
| nginx-ingress.ingress.configApiAdditionalAnnotations | object | `{}` | ConfigAPI ingress resource additional annotations. |
| nginx-ingress.ingress.configApiLabels | object | `{}` | configAPI ingress resource labels. key app is taken |
| nginx-ingress.ingress.deviceCodeAdditionalAnnotations | object | `{}` | device-code ingress resource additional annotations. |
| nginx-ingress.ingress.deviceCodeEnabled | bool | `true` | Enable endpoint /device-code |
| nginx-ingress.ingress.deviceCodeLabels | object | `{}` | device-code ingress resource labels. key app is taken |
| nginx-ingress.ingress.fido2ConfigAdditionalAnnotations | object | `{}` | fido2 config ingress resource additional annotations. |
| nginx-ingress.ingress.fido2ConfigEnabled | bool | `false` | Enable endpoint /.well-known/fido2-configuration |
| nginx-ingress.ingress.fido2ConfigLabels | object | `{}` | fido2 config ingress resource labels. key app is taken |
| nginx-ingress.ingress.firebaseMessagingAdditionalAnnotations | object | `{}` | Firebase Messaging ingress resource additional annotations. |
| nginx-ingress.ingress.firebaseMessagingEnabled | bool | `true` | Enable endpoint /firebase-messaging-sw.js |
| nginx-ingress.ingress.firebaseMessagingLabels | object | `{}` | Firebase Messaging ingress resource labels. key app is taken |
| nginx-ingress.ingress.openidAdditionalAnnotations | object | `{}` | openid-configuration ingress resource additional annotations. |
| nginx-ingress.ingress.openidConfigEnabled | bool | `true` | Enable endpoint /.well-known/openid-configuration |
| nginx-ingress.ingress.openidConfigLabels | object | `{}` | openid-configuration ingress resource labels. key app is taken |
| nginx-ingress.ingress.scimAdditionalAnnotations | object | `{}` | SCIM ingress resource additional annotations. |
| nginx-ingress.ingress.scimConfigAdditionalAnnotations | object | `{}` | SCIM config ingress resource additional annotations. |
| nginx-ingress.ingress.scimConfigEnabled | bool | `false` | Enable endpoint /.well-known/scim-configuration |
| nginx-ingress.ingress.scimConfigLabels | object | `{}` | SCIM config ingress resource labels. key app is taken |
| nginx-ingress.ingress.scimEnabled | bool | `false` | Enable SCIM endpoints /jans-scim |
| nginx-ingress.ingress.scimLabels | object | `{}` | SCIM config ingress resource labels. key app is taken |
| nginx-ingress.ingress.tls | list | `[{"hosts":["demoexample.jans.io"],"secretName":"tls-certificate"}]` | Secrets holding HTTPS CA cert and key. |
| nginx-ingress.ingress.u2fAdditionalAnnotations | object | `{}` | u2f config ingress resource additional annotations. |
| nginx-ingress.ingress.u2fConfigEnabled | bool | `true` | Enable endpoint /.well-known/fido-configuration |
| nginx-ingress.ingress.u2fConfigLabels | object | `{}` | u2f config ingress resource labels. key app is taken |
| nginx-ingress.ingress.uma2AdditionalAnnotations | object | `{}` | uma2 config ingress resource additional annotations. |
| nginx-ingress.ingress.uma2ConfigEnabled | bool | `true` | Enable endpoint /.well-known/uma2-configuration |
| nginx-ingress.ingress.uma2ConfigLabels | object | `{}` | uma2 config ingress resource labels. key app is taken |
| nginx-ingress.ingress.webdiscoveryAdditionalAnnotations | object | `{}` | webdiscovery ingress resource additional annotations. |
| nginx-ingress.ingress.webdiscoveryEnabled | bool | `true` | Enable endpoint /.well-known/simple-web-discovery |
| nginx-ingress.ingress.webdiscoveryLabels | object | `{}` | webdiscovery ingress resource labels. key app is taken |
| nginx-ingress.ingress.webfingerAdditionalAnnotations | object | `{}` | webfinger ingress resource additional annotations. |
| nginx-ingress.ingress.webfingerEnabled | bool | `true` | Enable endpoint /.well-known/webfinger |
| nginx-ingress.ingress.webfingerLabels | object | `{}` | webfinger ingress resource labels. key app is taken |
| opendj | object | `{"additionalAnnotations":{},"additionalLabels":{},"backup":{"cronJobSchedule":"*/59 * * * *","enabled":true},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenfederation/opendj","tag":"1.0.0_dev"},"livenessProbe":{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"failureThreshold":20,"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"multiCluster":{"clusterId":"","enabled":false,"namespaceIntId":0,"replicaCount":1,"serfAdvertiseAddrSuffix":"regional.janssen.org:30946","serfKey":"Z51b6PgKU1MZ75NCZOTGGoc0LP2OF3qvF6sjxHyQCYk=","serfPeers":["janssen-opendj-regional-0-regional.janssen.org:30946","janssen-opendj-regional-0-regional.janssen.org:31946"]},"persistence":{"size":"5Gi"},"ports":{"tcp-admin":{"nodePort":"","port":4444,"protocol":"TCP","targetPort":4444},"tcp-ldap":{"nodePort":"","port":1389,"protocol":"TCP","targetPort":1389},"tcp-ldaps":{"nodePort":"","port":1636,"protocol":"TCP","targetPort":1636},"tcp-repl":{"nodePort":"","port":8989,"protocol":"TCP","targetPort":8989},"tcp-serf":{"nodePort":"","port":7946,"protocol":"TCP","targetPort":7946},"udp-serf":{"nodePort":"","port":7946,"protocol":"UDP","targetPort":7946}},"readinessProbe":{"failureThreshold":20,"initialDelaySeconds":60,"periodSeconds":25,"tcpSocket":{"port":1636},"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"1500m","memory":"2000Mi"},"requests":{"cpu":"1500m","memory":"2000Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | OpenDJ is a directory server which implements a wide range of Lightweight Directory Access Protocol and related standards, including full compliance with LDAPv3 but also support for Directory Service Markup Language (DSMLv2).Written in Java, OpenDJ offers multi-master replication, access control, and many extensions. |
| opendj.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| opendj.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| opendj.backup | object | `{"cronJobSchedule":"*/59 * * * *","enabled":true}` | Configure ldap backup cronjob |
| opendj.dnsConfig | object | `{}` | Add custom dns config |
| opendj.dnsPolicy | string | `""` | Add custom dns policy |
| opendj.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| opendj.hpa.behavior | object | `{}` | Scaling Policies |
| opendj.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| opendj.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| opendj.image.pullSecrets | list | `[]` | Image Pull Secrets |
| opendj.image.repository | string | `"janssenfederation/opendj"` | Image  to use for deploying. |
| opendj.image.tag | string | `"1.0.0_dev"` | Image  tag to use for deploying. |
| opendj.livenessProbe | object | `{"exec":{"command":["python3","/app/scripts/healthcheck.py"]},"failureThreshold":20,"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the liveness healthcheck for OpenDJ if needed. https://github.com/JanssenFederation/docker-opendj/blob/master/scripts/healthcheck.py |
| opendj.livenessProbe.exec | object | `{"command":["python3","/app/scripts/healthcheck.py"]}` | Executes the python3 healthcheck. |
| opendj.multiCluster.clusterId | string | `""` | This id needs to be unique to each kubernetes cluster in a multi cluster setup west, east, south, north, region ...etc If left empty it will be randomly generated. |
| opendj.multiCluster.enabled | bool | `false` | Enable OpenDJ multiCluster mode. This flag enables loading keys under `opendj.multiCluster` |
| opendj.multiCluster.namespaceIntId | int | `0` | Namespace int id. This id needs to be a unique number 0-9 per janssen installation per namespace. Used when janssen is installed in the same kubernetes cluster more than once. |
| opendj.multiCluster.replicaCount | int | `1` | The number of opendj non scalabble statefulsets to create. Each pod created must be resolvable as it follows the patterm RELEASE-NAME-opendj-regional-{{statefulset pod number}}-{{ $.Values.multiCluster.serfAdvertiseAddrSuffix }} If set to 1, with a release name of janssen,  the address of the pod would be janssen-opendj-regional-0-regional.janssen.org |
| opendj.multiCluster.serfAdvertiseAddrSuffix | string | `"regional.janssen.org:30946"` | OpenDJ Serf advertise address suffix that will be added to each opendj replica. i.e RELEASE-NAME-opendj-regional-{{statefulset pod number}}-{{ $.Values.multiCluster.serfAdvertiseAddrSuffix }} |
| opendj.multiCluster.serfKey | string | `"Z51b6PgKU1MZ75NCZOTGGoc0LP2OF3qvF6sjxHyQCYk="` | Serf key. This key will automatically sync across clusters. |
| opendj.multiCluster.serfPeers | list | `["janssen-opendj-regional-0-regional.janssen.org:30946","janssen-opendj-regional-0-regional.janssen.org:31946"]` | Serf peer addresses. One per cluster. |
| opendj.persistence.size | string | `"5Gi"` | OpenDJ volume size |
| opendj.readinessProbe | object | `{"failureThreshold":20,"initialDelaySeconds":60,"periodSeconds":25,"tcpSocket":{"port":1636},"timeoutSeconds":5}` | Configure the readiness healthcheck for OpenDJ if needed. https://github.com/JanssenFederation/docker-opendj/blob/master/scripts/healthcheck.py |
| opendj.replicas | int | `1` | Service replica number. |
| opendj.resources | object | `{"limits":{"cpu":"1500m","memory":"2000Mi"},"requests":{"cpu":"1500m","memory":"2000Mi"}}` | Resource specs. |
| opendj.resources.limits.cpu | string | `"1500m"` | CPU limit. |
| opendj.resources.limits.memory | string | `"2000Mi"` | Memory limit. |
| opendj.resources.requests.cpu | string | `"1500m"` | CPU request. |
| opendj.resources.requests.memory | string | `"2000Mi"` | Memory request. |
| opendj.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| opendj.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| opendj.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| opendj.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| opendj.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| persistence | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/persistence-loader","tag":"1.0.0-beta.16"},"resources":{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | Job to generate data and intial config for Janssen Server persistence layer. |
| persistence.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| persistence.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| persistence.dnsConfig | object | `{}` | Add custom dns config |
| persistence.dnsPolicy | string | `""` | Add custom dns policy |
| persistence.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| persistence.image.pullSecrets | list | `[]` | Image Pull Secrets |
| persistence.image.repository | string | `"janssenproject/persistence-loader"` | Image  to use for deploying. |
| persistence.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| persistence.resources | object | `{"limits":{"cpu":"300m","memory":"300Mi"},"requests":{"cpu":"300m","memory":"300Mi"}}` | Resource specs. |
| persistence.resources.limits.cpu | string | `"300m"` | CPU limit |
| persistence.resources.limits.memory | string | `"300Mi"` | Memory limit. |
| persistence.resources.requests.cpu | string | `"300m"` | CPU request. |
| persistence.resources.requests.memory | string | `"300Mi"` | Memory request. |
| persistence.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| persistence.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| persistence.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| persistence.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| persistence.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |
| scim | object | `{"additionalAnnotations":{},"additionalLabels":{},"dnsConfig":{},"dnsPolicy":"","hpa":{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50},"image":{"pullPolicy":"IfNotPresent","pullSecrets":[],"repository":"janssenproject/scim","tag":"1.0.0-beta.16"},"livenessProbe":{"httpGet":{"path":"/jans-scim/sys/health-check","port":8080},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5},"readinessProbe":{"httpGet":{"path":"/jans-scim/sys/health-check","port":8080},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5},"replicas":1,"resources":{"limits":{"cpu":"1000m","memory":"1000Mi"},"requests":{"cpu":"1000m","memory":"1000Mi"}},"service":{"name":"http-scim","port":8080},"usrEnvs":{"normal":{},"secret":{}},"volumeMounts":[],"volumes":[]}` | System for Cross-domain Identity Management (SCIM) version 2.0 |
| scim.additionalAnnotations | object | `{}` | Additional annotations that will be added across the gateway in the format of {cert-manager.io/issuer: "letsencrypt-prod"} |
| scim.additionalLabels | object | `{}` | Additional labels that will be added across the gateway in the format of {mylabel: "myapp"} |
| scim.dnsConfig | object | `{}` | Add custom dns config |
| scim.dnsPolicy | string | `""` | Add custom dns policy |
| scim.hpa | object | `{"behavior":{},"enabled":true,"maxReplicas":10,"metrics":[],"minReplicas":1,"targetCPUUtilizationPercentage":50}` | Configure the HorizontalPodAutoscaler |
| scim.hpa.behavior | object | `{}` | Scaling Policies |
| scim.hpa.metrics | list | `[]` | metrics if targetCPUUtilizationPercentage is not set |
| scim.image.pullPolicy | string | `"IfNotPresent"` | Image pullPolicy to use for deploying. |
| scim.image.pullSecrets | list | `[]` | Image Pull Secrets |
| scim.image.repository | string | `"janssenproject/scim"` | Image  to use for deploying. |
| scim.image.tag | string | `"1.0.0-beta.16"` | Image  tag to use for deploying. |
| scim.livenessProbe | object | `{"httpGet":{"path":"/jans-scim/sys/health-check","port":8080},"initialDelaySeconds":30,"periodSeconds":30,"timeoutSeconds":5}` | Configure the liveness healthcheck for SCIM if needed. |
| scim.livenessProbe.httpGet.path | string | `"/jans-scim/sys/health-check"` | http liveness probe endpoint |
| scim.readinessProbe | object | `{"httpGet":{"path":"/jans-scim/sys/health-check","port":8080},"initialDelaySeconds":25,"periodSeconds":25,"timeoutSeconds":5}` | Configure the readiness healthcheck for the SCIM if needed. |
| scim.readinessProbe.httpGet.path | string | `"/jans-scim/sys/health-check"` | http readiness probe endpoint |
| scim.replicas | int | `1` | Service replica number. |
| scim.resources.limits.cpu | string | `"1000m"` | CPU limit. |
| scim.resources.limits.memory | string | `"1000Mi"` | Memory limit. |
| scim.resources.requests.cpu | string | `"1000m"` | CPU request. |
| scim.resources.requests.memory | string | `"1000Mi"` | Memory request. |
| scim.service.name | string | `"http-scim"` | The name of the scim port within the scim service. Please keep it as default. |
| scim.service.port | int | `8080` | Port of the scim service. Please keep it as default. |
| scim.usrEnvs | object | `{"normal":{},"secret":{}}` | Add custom normal and secret envs to the service |
| scim.usrEnvs.normal | object | `{}` | Add custom normal envs to the service variable1: value1 |
| scim.usrEnvs.secret | object | `{}` | Add custom secret envs to the service variable1: value1 |
| scim.volumeMounts | list | `[]` | Configure any additional volumesMounts that need to be attached to the containers |
| scim.volumes | list | `[]` | Configure any additional volumes that need to be attached to the pod |

----------------------------------------------
Autogenerated from chart metadata using [helm-docs v1.7.0](https://github.com/norwoodj/helm-docs/releases/v1.7.0)

