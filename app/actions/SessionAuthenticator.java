package actions;


import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;


/**
 * Created by muneeb on 26/01/17.
 */
public class SessionAuthenticator extends Security.Authenticator {

    private static final Logger LOG = LoggerFactory.getLogger(SessionAuthenticator.class);
    @Override
    public String getUsername(Http.Context ctx) {
        String userId = getTokenFromHeader(ctx);
        if (userId != null) {
            User user = User.find.where().eq("id", userId).findUnique();
            if (user != null) {
                return Long.toString(user.id);
            }
        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return redirect("/admin");
    }

    private String getTokenFromHeader(Http.Context ctx) {
        return ctx.session().get("connected");
    }
}