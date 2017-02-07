package actions;


import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;

/**
 * Created by muneeb on 26/01/17.
 */
public class ActionAuthenticator extends Security.Authenticator {

    private static final Logger LOG = LoggerFactory.getLogger(ActionAuthenticator.class);
    @Override
    public String getUsername(Http.Context ctx) {
        String token = getTokenFromHeader(ctx);
        token = token.substring(0,token.lastIndexOf(":"));
        if (token != null) {
            User user = User.find.where().eq("access_token", token).findUnique();
            if (user != null) {
                return Long.toString(user.id);
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String accessToken = null;
        String authHeader = ctx.request().getHeader("Authorization");
        if (authHeader != null) {
            StringTokenizer st = new StringTokenizer(authHeader);
            if (st.hasMoreTokens()) {
                String basic = st.nextToken();
                if (basic.equalsIgnoreCase("Basic")) {
                    try {
                         accessToken = new String(Base64.decodeBase64(st.nextToken()), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        throw new Error("Couldn't retrieve authentication", e);
                    }
                }
            }
        }

        return accessToken;
    }
}