package actions;


import models.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Created by muneeb on 26/01/17.
 */
public class SessionAuthenticator extends Security.Authenticator {

    private static final Logger LOG = LoggerFactory.getLogger(ActionAuthenticator.class);
    @Override
    public String getUsername(Http.Context ctx) {
        User user = null;
        String access_token = getAccessTokenFromBasicAuth(ctx);
        if (access_token != null) {
            user = User.find.where().eq("access_token", access_token).findUnique();
        } else {
            String userId = getTokenFromSession(ctx);
            if(userId != null)
                user = User.find.where().eq("id", userId).findUnique();
        }
        if (user != null) {
            return Long.toString(user.id);
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }


    private String getAccessTokenFromBasicAuth(Http.Context ctx) {
        String authorization = ctx.request().getHeader("Authorization");
        LOG.info("Authorization is {}",authorization);
        if (authorization != null && authorization.startsWith("Basic")) {
            // Authorization: Basic base64credentials
            String base64Credentials = authorization.substring("Basic".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials),
                    Charset.forName("UTF-8"));
            final String[] values = credentials.split(":", 2);
            LOG.info("Credential values {}",values);
            return values[0];
        }
        return null;
    }
    private String getTokenFromSession(Http.Context ctx) {
        return ctx.session().get("connected");
    }
}