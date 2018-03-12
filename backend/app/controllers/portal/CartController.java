package controllers.portal;

import actions.SessionAuthenticator;
import models.cart.Cart;
import models.product.Category;
import models.user.User;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import java.util.List;

/**
 * Created by muneeb on 20/02/17.
 */
public class CartController extends Controller{

    @Security.Authenticated(SessionAuthenticator.class)
    public Result cart() {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        List<Category> categories = Category.find.all();
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        return Results.ok(views.html.portal.cart.render(user,cart,categories));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result checkout() {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        List<Category> categories = Category.find.all();
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        return Results.ok(views.html.portal.checkout.render(user,cart,categories));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result addToCart() {
        String userId = session("connected");
        User user = null;
        if(user == null) {
            return badRequest();
        }
        return badRequest();
    }
}
