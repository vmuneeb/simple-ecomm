package actions;


import models.user.User;
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
        String userId = getTokenFromSession(ctx);
        if (userId != null) {
            User user = User.find.where().eq("id", Integer.parseInt(userId)).findUnique();
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


    private String getTokenFromSession(Http.Context ctx) {
        return ctx.session().get("admin");
    }
}