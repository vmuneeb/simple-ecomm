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

    public Result loginUser() {

        Form<LoginDto> form = formFactory.form(LoginDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        LoginDto loginDto = form.get();
        User user = User.find.where().eq("email",loginDto.email).eq("password",loginDto.password).findUnique();
        if(user == null) {
            return Results.badRequest("user not found");
        }else {
            return Results.ok(Json.toJson(user));
        }
    }


    public Result registerUser() {

        Form<SignupDto> form = formFactory.form(SignupDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        SignupDto signupDto = form.get();
        User user = User.find.where().eq("email",signupDto.username).findUnique();
        if(user != null) {
            return Results.badRequest("user already registered");
        }
        user = new User();
        user.email = signupDto.username;
        user.password = signupDto.password;
        user.gender  = signupDto.gender ;
        user.accessToken = UUID.randomUUID().toString();
        user.save();
        return Results.ok(Json.toJson(user));
    }


    public Result getUserDetails(String id) {

        User user = User.find.all().get(0);
        if(user == null) {
            return Results.notFound("No user found");
        }
        return Results.ok(Json.toJson(user));
    }

}
