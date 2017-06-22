package controllers.api;

import Util.MailUtility;
import actions.ActionAuthenticator;
import actions.SessionAuthenticator;
import com.google.inject.Inject;
import dto.ChangePasswordDto;
import dto.LoginDto;
import dto.SignupDto;
import dto.UserDto;
import models.user.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;


import java.util.UUID;

/**
 * Created by muneeb on 07/01/17.
 */
public class UserController extends Controller {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Inject
    FormFactory formFactory;

    @Inject
    MailUtility mailUtility;

    @BodyParser.Of(BodyParser.Json.class)
    public Result loginUser() {
        LOG.info("In login user");

        Form<LoginDto> form = formFactory.form(LoginDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        LoginDto loginDto = form.get();
        LOG.info("login email {}",loginDto.email);

        User user = User.find.where().eq("email",loginDto.email).findUnique();
        if(user != null && BCrypt.checkpw(loginDto.password,user.password)) {
            return Results.ok(Json.toJson(user));
        }
        return Results.badRequest("Login failed");
    }

    public Result logout() {
        session().clear();
        return ok();
    }



    @BodyParser.Of(BodyParser.Json.class)
    public Result registerUser() {
        LOG.info("In registerUser");

        Form<SignupDto> form = formFactory.form(SignupDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        SignupDto signupDto = form.get();

        LOG.info("User details are {},{},{}",signupDto.email,signupDto.name,signupDto.password);

        User user = User.find.where().eq("email",signupDto.email).findUnique();
        if(user != null) {
            return Results.badRequest("user already registered");
        }
        user = new User();
        user.email = signupDto.email;
        user.name = signupDto.name;
        user.password = BCrypt.hashpw(signupDto.password, BCrypt.gensalt());
        user.gender  = signupDto.gender ;
        user.accessToken = UUID.randomUUID().toString();
        user.save();
        return Results.ok(Json.toJson(user));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result getUserDetails(String id) {
        String userId = request().username();
        LOG.info("user is {}",userId);
        User user = User.find.where().eq("id",Integer.parseInt(userId)).findUnique();
        if(user == null) {
            return Results.notFound("No user found");
        }
        return Results.ok(Json.toJson(user));
    }

    @Security.Authenticated(SessionAuthenticator.class)
    public Result changePassword(String id) {
        LOG.info("In changePassword");
        Form<ChangePasswordDto> form = formFactory.form(ChangePasswordDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        String userId = request().username();
        ChangePasswordDto changePasswordDto = form.get();
        LOG.info("Change password details {},{}",changePasswordDto.old_password,changePasswordDto.new_password);
        User user = User.find.where().eq("id",Integer.parseInt(userId)).findUnique();
        if(user == null) {
            Results.badRequest("No user");
        }
        if(user.password.equalsIgnoreCase(changePasswordDto.old_password)) {
            user.password = BCrypt.hashpw(changePasswordDto.new_password, BCrypt.gensalt());
            user.save();
        }
        return Results.ok(Json.toJson(user));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result editUserDetails(String id) {
        Form<UserDto> form = formFactory.form(UserDto.class).bindFromRequest();
        LOG.info(request().body().asJson().toString());

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        String userId = request().username();
        LOG.info("user is {}",userId);
        UserDto userDto = form.get();
        LOG.info("User details are {}",userDto.name);
        User user = User.find.where().eq("id",Integer.parseInt(userId)).findUnique();
        if(user != null) {
            user.name = userDto.name;
            user.phone = userDto.phone;
            user.building = userDto.building;
            user.street = userDto.street;
            user.area = userDto.area;
            user.city = userDto.city;
            user.save();
            return Results.ok(Json.toJson(user));
        }
        return Results.notFound("No user found");

    }

}
