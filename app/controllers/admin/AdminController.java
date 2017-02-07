package controllers.admin;


import actions.SessionAuthenticator;
import com.google.inject.Inject;
import dto.LoginDto;
import models.User;
import models.UserType;
import models.order.Order;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import java.util.List;

/**
 * Created by muneeb on 30/01/17.
 */
public class AdminController extends Controller{

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Inject
    FormFactory formFactory;

    public Result index() {
        String user = session("connected");
        if(user != null) {
            return redirect("/admin/home");
        }
        return Results.ok(views.html.admin.index.render());
    }

    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public Result loginAdmin() {
        LOG.info("In login admin");

        Form<LoginDto> form = formFactory.form(LoginDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        LoginDto loginDto = form.get();
        LOG.info("login email {}",loginDto.email);

        User user = User.find.where().eq("email",loginDto.email).eq("user_type", UserType.ADMIN.ordinal()).findUnique();  //allow only admins
        if(user != null && BCrypt.checkpw(loginDto.password,user.password)) {
            session("connected",Long.toString(user.id));
            return redirect("/admin/home");
        }
        return Results.ok(views.html.admin.index.render());
    }

    public Result logout() {
        session().clear();
        return Results.ok(views.html.admin.index.render());
    }

    @Security.Authenticated(SessionAuthenticator.class)
    public Result home() {
        User user = User.find.where().eq("id",session("connected")).findUnique();
        List<Order> orders = Order.find.where().eq("user_id",user.id).findList();
        return Results.ok(views.html.admin.home.render(user,orders));
    }
}
