import io.quarkus.oidc.TenantConfigResolver;
import io.quarkus.oidc.OidcTenantConfig;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomTenantConfigResolver implements TenantConfigResolver {

    @Override
    public OidcTenantConfig resolve(RoutingContext context) {

        //
        // 1. docker run -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:11.0.2
        /*
           2. in browser run http://localhost:8180/auth/

           3. import these 2 files
            https://github.com/quarkusio/quarkus-quickstarts/blob/master/security-openid-connect-multi-tenancy/config/default-tenant-realm.json
            https://raw.githubusercontent.com/quarkusio/quarkus-quickstarts/master/security-openid-connect-multi-tenancy/config/default-tenant-realm.json

            in the keycloak system
            select Realm
            click "Add Realm"
            and then import the file
        */


        String path = context.request().path();
        String[] parts = path.split("/");

        if (parts.length == 0) {
            // resolve to default tenant configuration
            return null;
        }
        if ("tenant-a".equals(parts[1])) {
            OidcTenantConfig config = new OidcTenantConfig();
            config.setTenantId("tenant-a");

            config.setAuthServerUrl("http://localhost:8180/auth/realms/tenant-a");
            config.setClientId("multi-tenant123");
            config.applicationType = OidcTenantConfig.ApplicationType.WEB_APP;
            config.setDiscoveryEnabled(false);
            config.setAuthorizationPath("protocol/openid-connect/auth");
            config.setTokenPath("protocol/openid-connect/token");

            // I dive into source code for 3 days research, leave it empty!!!!!!!, looks like a bug, I created a ticket to the author let him check it
            // https://github.com/quarkusio/quarkus/issues/12754
            //config.setJwksPath("protocol/openid-connect/certs");


            config.setUserInfoPath("protocol/openid-connect/userinfo");
            config.setIntrospectionPath("protocol/openid-connect/token/introspect");

           // create the client id MUST WITH Access Type confidential
            OidcTenantConfig.Credentials credentials = new OidcTenantConfig.Credentials();
            credentials.setSecret("89cbfed7-0f61-4f26-86ea-2c4053e6d881");
            config.setCredentials(credentials);

            return config;
        }


        if ("tenant-c".equals(parts[1])) {
            OidcTenantConfig config = new OidcTenantConfig();
            config.setTenantId("tenant-c");

            config.setAuthServerUrl("http://localhost:8180/auth/realms/tenant-c");
            config.setClientId("multi-tenantx");
            config.applicationType = OidcTenantConfig.ApplicationType.WEB_APP;
            config.setDiscoveryEnabled(false);
            config.setAuthorizationPath("protocol/openid-connect/auth");
            config.setTokenPath("protocol/openid-connect/token");
            //config.setJwksPath("protocol/openid-connect/certs"); // keep it empty
            config.setUserInfoPath("protocol/openid-connect/userinfo");
            config.setIntrospectionPath("protocol/openid-connect/token/introspect");
             OidcTenantConfig.Credentials credentials = new OidcTenantConfig.Credentials();
            credentials.setSecret("b974ebbf-c840-470b-a458-0ffacd4c6929");
            config.setCredentials(credentials);

            return config;
        }



        // resolve to default tenant configuration
        return null;
    }
}