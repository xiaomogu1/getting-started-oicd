import io.quarkus.oidc.IdToken;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/{tenant}")
public class HomeResource {

    /**
     * Injection point for the ID Token issued by the OpenID Connect Provider
     */
    @Inject
    @IdToken
    JsonWebToken idToken;

     // docker run -p 8180:8080 -e KEYCLOAK_USER=admin -e KEYCLOAK_PASSWORD=admin quay.io/keycloak/keycloak:11.0.2

    //docker exec -it <mycontainer> bash

    @GET
    public String getHome() {
        StringBuilder response = new StringBuilder().append("<html>").append("<body>");

        response.append("<h2>Welcome, ").append(this.idToken.getClaim("email").toString()).append("</h2>\n");
        response.append("<h3>You are accessing the application within tenant <b>").append(idToken.getIssuer()).append(" boundaries</b></h3>");
        response.append("<h2>full info, ").append(this.idToken ).append("</h2>\n");
        String[] vals = this.idToken.getIssuer().split("[/]");
        int last  = vals.length -1;
        String tenant = vals[last];
        String logout = this.idToken.getIssuer()+"/protocol/openid-connect/logout?redirect_uri=http://localhost:8080/"+ tenant;
        response.append("<a href = "+logout+">Logout</a>");
        return response.append("</body>").append("</html>").toString();
    }
}