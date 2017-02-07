package controllers.api;


import Util.Utility;
import actions.ActionAuthenticator;
import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dto.CartDto;
import models.User;
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

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addToCart() {
        Form<CartDto> form = formFactory.form(CartDto.class).bindFromRequest();
        String userId = request().username();
        if (form.hasErrors()) {
            return Results.badRequest();
        }
        CartDto cartDto = form.get();
        LOG.info("Add {} to cart",cartDto.product_variant_id);
        Product product = Product.find.where().eq("id",cartDto.product_variant_id).findUnique();
        if(product == null) {
            return badRequest("No product found");
        }

        Cart cart = Cart.find.where().eq("user_id",userId).findUnique();
        if(cart == null) {
            cart = new Cart();
            User user= User.find.where().eq("id",userId).findUnique();
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
            User user= User.find.where().eq("id",userId).findUnique();
            if(user == null) {
                return unauthorized();
            }
            cart.user = user;
        }


        CartProduct cartProduct;
        if(old == null ) {
            cartProduct = new CartProduct();
        } else {
            cartProduct = old;
        }
        cartProduct.productId = product.id;
        cartProduct.price = product.price;
        cartProduct.formattedPrice = Utility.getFormattedPrice(product.price);
        cartProduct.discountPrice = product.discountPrice;
        cartProduct.mainImage = product.mainImage;
        cartProduct.quantity = cartDto.quantity;
        cartProduct.name = product.name;
        cartProducts.add(cartProduct);
        cart.items = cartProducts;
        int total = 0;
        int quantity = 0;
        for (CartProduct item : cart.items) {
            total=total+(int)item.price*item.quantity;
            quantity = quantity+item.quantity;
        }
        cart.productCount = quantity;
        cart.totalPrice = total;
        cart.totalPriceFormatted = Utility.getFormattedPrice(total);
        cart.save();
        return Results.ok(Json.toJson(cart));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteFromCart(String cartProductId) {
        LOG.info("In deleteFromCart");
        LOG.error("Delete {}",cartProductId);

        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",userId).findUnique();
        LOG.error("Cart id  {}",cart.id);
        if(cart == null) {
            return notFound();
        }
        CartProduct cartProduct = CartProduct.find.where().and(Expr.eq("id",cartProductId),Expr.eq("cart_id",cart.id)).findUnique();
        if(cartProduct != null) {
            LOG.error("Deleted : {}",cartProduct.delete());
        } else {
            LOG.error("Not Deleted");
        }

        int total = 0;
        int productCount = 0;
        for (CartProduct item : cart.items) {
            total=total+(int)item.price*item.quantity;
            productCount+=item.quantity;
        }
        cart.totalPrice = total;
        cart.productCount = productCount;
        cart.totalPriceFormatted = Utility.getFormattedPrice(total);
        cart.save();
        return Results.ok(Json.toJson(cart));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getCart() throws Exception{
        LOG.info("In getCart");
        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",userId).findUnique();
        if(cart != null) {
            return Results.ok(Json.toJson(cart));
        }
        cart = new Cart();
        cart.productCount = 0;
        LOG.info("Cart is {}",Json.toJson(cart));
        return Results.ok(Json.toJson(cart));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result getCartInfo() throws Exception{
        LOG.info("In getCartInfo");
        String userId = request().username();
        Cart cart = Cart.find.where().eq("user_id",userId).findUnique();
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
