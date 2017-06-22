package controllers.api;


import Util.Utility;
import actions.ActionAuthenticator;
import actions.SessionAuthenticator;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dto.CartDto;
import models.user.User;
import models.cart.Cart;
import models.cart.CartProduct;
import models.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by muneeb on 07/01/17.
 */
public class CartController extends Controller {

    @Inject
    FormFactory formFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Security.Authenticated(SessionAuthenticator.class)
    public Result addToCart() {
        Form<CartDto> form = formFactory.form(CartDto.class).bindFromRequest();
        String userId = request().username();
        if (form.hasErrors()) {
            LOG.error("Form has errors {}",form.errors());
            return Results.badRequest();
        }
        CartDto cartDto = form.get();
        LOG.info("Add {} to cart",cartDto.product_variant_id);
        Product product = Product.find.where().eq("id",cartDto.product_variant_id).findUnique();
        if(product == null) {
            return badRequest("No product found");
        }


        Cart cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        if(cart == null) {
            cart = new Cart();
            User user= User.find.where().eq("id",Integer.parseInt(userId)).findUnique();
            if(user == null) {
                return unauthorized();
            }
            cart.user = user;
        }

        CartProduct old = null;
        List<CartProduct> cartProducts = cart.items;
        if(cartProducts == null ) {
            cartProducts = new LinkedList<>();
        } else {
            old = CartProduct.find.where().eq("product_id",cartDto.product_variant_id).eq("cart_id",cart.id).findUnique();
        }

        if(cart == null) {
            cart = new Cart();
            User user= User.find.where().eq("id",Integer.parseInt(userId)).findUnique();
            if(user == null) {
                return unauthorized();
            }
            cart.user = user;
        }


        CartProduct cartProduct  = new CartProduct();
        if(old != null  ) {
            cartProducts.remove(old);
        }
        if(product.quantity <= 0 ) {
            return badRequest("Out of stock");
        }
        cartProduct.productId = product.id;
        cartProduct.price = product.price;
        cartProduct.formattedPrice = Utility.getFormattedPrice(product.price);
        cartProduct.discountPrice = product.discountPrice;
        cartProduct.discountPriceFormatted = Utility.getFormattedPrice(product.discountPrice);
        cartProduct.mainImage = product.getImage();
        cartProduct.quantity = cartDto.quantity;
        cartProduct.name = product.name;
        cartProducts.add(cartProduct);
        cartProducts.remove(old);
        cart.items = cartProducts;
        int total = 0;
        int quantity = 0;
        for (CartProduct item : cart.items) {
            total=total+(int)item.discountPrice*item.quantity;
            quantity = quantity+item.quantity;
        }
        cart.productCount = quantity;
        cart.totalPrice = total;
        cart.totalPriceFormatted = Utility.getFormattedPrice(total);
        cart.save();
        return Results.ok(Json.toJson(cart));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result deleteFromCart(String cartProductId) {
        LOG.info("In deleteFromCart");
        LOG.error("Delete {}",cartProductId);

        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        LOG.error("Cart id  {}",cart.id);
        if(cart == null) {
            return notFound("No cart");
        }
        CartProduct cartProduct = CartProduct.find.where().and(Expr.eq("id",cartProductId),Expr.eq("cart_id",cart.id)).findUnique();
        if(cartProduct != null) {
            LOG.error("Cart products empty?: {}",cart.items.isEmpty());
            cart.items.remove(cartProduct);
            LOG.error("Cart products empty?: {}",cart.items.isEmpty());
            if(cart.items.isEmpty())
            {
                cart.delete();
                LOG.error("All items removed. Deleting cart");
            }
            LOG.error("Cart products after delete: {}",Json.toJson(cart));
        } else {
            LOG.error("Deletion failed");
        }

        int total = 0;
        int productCount = 0;
        for (CartProduct item : cart.items) {
            total=total+(int)item.price*item.quantity;
            productCount+=item.quantity;
        }
        LOG.error("Updaing cart with total");
        cart.totalPrice = total;
        cart.productCount = productCount;
        cart.totalPriceFormatted = Utility.getFormattedPrice(total);
        cart.save();
        return Results.ok(Json.toJson(cart));
    }

    @Security.Authenticated(SessionAuthenticator.class)
    public Result getCart() throws Exception{
        LOG.info("In getCart");
        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        if(cart != null) {
            return Results.ok(Json.toJson(cart));
        }
        cart = new Cart();
        cart.productCount = 0;
        LOG.info("Cart is {}",Json.toJson(cart));
        return Results.ok(Json.toJson(cart));
    }


    @Security.Authenticated(SessionAuthenticator.class)
    public Result getCartInfo() throws Exception{
        LOG.info("In getCartInfo");
        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        if(cart == null) {
            return notFound();
        }
        ObjectNode res = Json.newObject();
        res.put("product_count",cart.items.size());
        res.put("total_price",cart.totalPrice);
        res.put("total_price_formatted",cart.totalPriceFormatted);
        LOG.info("Cart info is {}",Json.toJson(cart));
        return Results.ok(res);
    }
}
