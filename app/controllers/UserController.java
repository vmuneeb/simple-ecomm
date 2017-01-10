package controllers;

import com.google.inject.Inject;
import dto.LoginDto;
import dto.SignupDto;
import models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.UUID;

/**
 * Created by muneeb on 07/01/17.
 */
public class UserController extends Controller {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Inject
    FormFactory formFactory;

    @BodyParser.Of(BodyParser.Json.class)
    public Result loginUser() {

        LOG.error("request body is {}",request().body().asJson());

        Form<LoginDto> form = formFactory.form(LoginDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        LoginDto loginDto = form.get();
        User user = User.find.where().eq("email",loginDto.email).eq("password",loginDto.password).findUnique();
        if(user != null) {
            return Results.badRequest("user already registered");
        }else {
            return Results.ok(Json.toJson(user));
        }
    }


    @BodyParser.Of(BodyParser.Json.class)
    public Result registerUser() {
        LOG.error("request body is {}",request().body().asJson());

        Form<SignupDto> form = formFactory.form(SignupDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        SignupDto signupDto = form.get();
        User user = User.find.where().eq("email",signupDto.email).findUnique();
        if(user != null) {
            return Results.badRequest("user already registered");
        }
        user = new User();
        user.setEmail(signupDto.email);
        user.setPhone(signupDto.password);
        user.setGender(signupDto.gender);
        user.setAccessToken(UUID.randomUUID().toString());
        return Results.ok(Json.toJson(user));
    }

}
