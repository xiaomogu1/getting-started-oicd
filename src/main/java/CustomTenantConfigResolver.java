import io.quarkus.oidc.TenantConfigResolver;
import io.quarkus.oidc.OidcTenantConfig;
import io.vertx.ext.web.RoutingContext;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomTenantConfigResolver implements TenantConfigResolver {

    @Override
    public OidcTenantConfig resolve(RoutingContext context) {
        String path = context.request().path();
        String[] parts = path.split("/");

        if (parts.length == 0) {
            // resolve to default tenant configuration
            return null;
        }
        System.out.println("print here resolve");
        if ("tenant-a".equals(parts[1])) {

            System.out.println("print here tenant-a");
            OidcTenantConfig config = new OidcTenantConfig();
            config.setTenantId("tenant-a");
            config.setAuthServerUrl("http://localhost:8180/auth/realms/tenant-a");
            config.setClientId("multi-tenant-client");
            config.applicationType = OidcTenantConfig.ApplicationType.WEB_APP;
            return config;
        }

        // resolve to default tenant configuration
        return null;
    }
}