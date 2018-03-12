package controllers.portal;

import Util.MailUtility;
import com.google.inject.Inject;
import dto.*;
import models.user.ResetPassWord;
import models.user.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.*;

import java.util.UUID;

/**
 * Created by muneeb on 16/02/17.
 */
public class UserController extends Controller{

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);
    @Inject
    FormFactory formFactory;

    @Inject
    MailUtility mailUtility;


    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public Result loginUser() {
        LOG.info("In login user");
        String message = null;

        Form<LoginDto> form = formFactory.form(LoginDto.class).bindFromRequest();

        if (form.hasErrors()) {
            message = "Please enter valid details";
            return Results.ok(views.html.portal.login.render(message));
        }

        LoginDto loginDto = form.get();
        LOG.info("login email {}",loginDto.email);

        User user = User.find.where().eq("email",loginDto.email).findUnique();
        if(user != null && BCrypt.checkpw(loginDto.password,user.password)) {
            session("connected",Long.toString(user.id));
            return redirect("/");
        }
        message = "email or password doesn't match";
        return Results.ok(views.html.portal.login.render(message));
    }



    @BodyParser.Of(BodyParser.FormUrlEncoded.class)
    public Result registerUser() {
        LOG.info("In registerUser");

        Form<SignupDto> form = formFactory.form(SignupDto.class).bindFromRequest();
        String message = null;

        if (form.hasErrors()) {
            message = "Please enter valid details";
            LOG.info("In registerUser1 with message {}",message);
            return Results.ok(views.html.portal.signup.render(message));
        }

        SignupDto signupDto = form.get();

        LOG.info("User details are {},{},{}",signupDto.email,signupDto.name,signupDto.password);
        User user = User.find.where().eq("email",signupDto.email).findUnique();
        if(user != null) {
            LOG.info("In registerUser1 with message {}",message);
            message = "user already registered !!";
            return Results.ok(views.html.portal.signup.render(message));
        }
        user = new User();
        user.email = signupDto.email;
        user.name = signupDto.name;
        user.password = BCrypt.hashpw(signupDto.password, BCrypt.gensalt());
        user.gender  = signupDto.gender ;
        user.accessToken = UUID.randomUUID().toString();
        user.save();
        session("connected",Long.toString(user.id));
        mailUtility.sendWelcomeMail(user);
        return redirect("/");
    }


    public Result logout() {
        session().clear();
        return redirect("/");
    }

    public Result signup() {
        return Results.ok(views.html.portal.signup.render(null));
    }


    public Result resetPwd() {
        LOG.info("In resetPwd");
        Form<ResetPassWordDto> form = formFactory.form(ResetPassWordDto.class).bindFromRequest();

        if (form.hasErrors()) {
            LOG.info("Form has errors {}",form.errors().toString());
            return Results.badRequest();
        }
        ResetPassWordDto resetDto = form.get();
        User user = User.find.where().eq("email",resetDto.email).findUnique();
        if(user != null ) {
            LOG.info("User is not null");
            ResetPassWord resetPassWord = ResetPassWord.find.where().eq("user_id",user.id).eq("token",resetDto.token).findUnique();
            if(resetPassWord != null) {
                LOG.info("All good changing password");
                user.password = BCrypt.hashpw(resetDto.password1, BCrypt.gensalt());
                user.save();
                LOG.info("Render reset");
                return Results.ok(views.html.portal.reset.render(null));
            }
        }
        return badRequest("Bad request");
    }

    public Result reset() {
        LOG.info("In reset");
        Form<ResetDto> form = formFactory.form(ResetDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        ResetDto resetDto = form.get();
        User user = User.find.where().eq("email",resetDto.email).findUnique();
        if(user != null ) {
            ResetPassWord resetPassWord = ResetPassWord.find.where().eq("user_id",user.id).eq("token",resetDto.token).findUnique();
            if(resetPassWord != null) {
                return Results.ok(views.html.portal.reset.render(resetPassWord));
            }
        }
         return badRequest("Bad request");
    }



    public Result forgotPassword() {
        LOG.info("In forgotPassword");
        Boolean success = false;
        return Results.ok(views.html.portal.forgot.render(null,success));
    }

    public Result forgotPwdSendEmail() {
        LOG.info("In forgotPwdSendEmail");

        Form<ForgotPassWordDto> form = formFactory.form(ForgotPassWordDto.class).bindFromRequest();
        String message = null;
        Boolean success = false;

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        ForgotPassWordDto forgotPassWordDto = form.get();
        User user = User.find.where().eq("email",forgotPassWordDto.email).findUnique();
        if(user != null) {
            success = true;
            ResetPassWord resetPassWord = ResetPassWord.find.where().eq("user_id",user.id).findUnique();
            if(resetPassWord == null) {
                resetPassWord = new ResetPassWord();
                resetPassWord.user = user;
                resetPassWord.token = UUID.randomUUID().toString();
            }
            resetPassWord.save();
            message = " Check your email for a link to reset your password. If it doesn't appear within a few minutes, check your spam folder.";
            mailUtility.sendPassWordResetEmail(resetPassWord);
            return Results.ok(views.html.portal.forgot.render(message,success));
        }
        message = " Can't find that email, sorry.";
        return Results.ok(views.html.portal.forgot.render(message,success));
    }
}
