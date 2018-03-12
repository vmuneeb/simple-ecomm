package controllers.portal;

import actions.SessionAuthenticator;
import com.avaje.ebean.Expr;
import com.google.inject.Inject;
import controllers.api.ProductController;
import models.banner.Banner;
import models.order.Order;
import models.product.Brand;
import models.product.SubCategory;
import models.user.User;
import models.cart.Cart;
import models.product.Category;
import models.product.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by muneeb on 30/01/17.
 */
public class HomeController extends Controller {

    private static final Logger LOG = LoggerFactory.getLogger(HomeController.class);

    private static String ORDER_FIELD = "updated_time";

    @Inject
    ProductController productController;

    public Result index() {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<Banner> banners = Banner.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        List<Product> products = Product.find.where().eq("featured",true).findList();
        if(products == null ) {
            products = Product.find.all();
        }
        return Results.ok(views.html.portal.home.render(user,cart,categories,subCategories,brands,products,banners));
    }

    public Result login() {
        return Results.ok(views.html.portal.login.render(null));
    }

    public Result product(String id) {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        Product product = Product.find.where().eq("id",id).findUnique();
        return Results.ok(views.html.portal.details.render(user,cart,categories,subCategories,brands,product));
    }

    public Result products(String category,String subCategory,String brand,String search,int pageNumber) {
        String userId = session("connected");
        String param = null;
        String value = null;

        if(category != null ) {
            param = "category";
            value = category;
        }else if(subCategory != null ) {
            param = "subCategory";
            value = subCategory;
        }else if(search != null ) {
            param = "search";
            value = search;
        }

        User user = null;
        Cart cart = null;
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        List<Product> products ;
        List<Category> categories = Category.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        List<Brand> brands = Brand.find.all();
        products = productController.getProducts(category, subCategory, brand, search,pageNumber);
        return Results.ok(views.html.portal.products.render(user,cart,categories,subCategories,brands,products,param,value));
    }

    @Security.Authenticated(SessionAuthenticator.class)
    public Result order(String id) {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        List<Category> categories = Category.find.all();
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        } else {
            return notFound();
        }
        Order order = null;
        if(user != null) {
            order = Order.find.where().eq("user_id",userId).eq("id",id).findUnique();
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
        }
        return Results.ok(views.html.portal.order.render(user,order,cart,categories));
    }

    @Security.Authenticated(SessionAuthenticator.class)
    public Result orders(int pageNumber) {
        String userId = session("connected");
        User user = null;
        Cart cart = null;
        List<Category> categories = Category.find.all();
        List<Order> orders = null;
        if(userId != null) {
            user = User.find.where().eq("id",Integer.parseInt(session("connected"))).findUnique();
        }
        if(user != null) {
            cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();
            orders = Order.find.where().eq("user_id",userId).setMaxRows(10).setFirstRow(pageNumber*10).orderBy("created_time desc").findList();
        }
        return Results.ok(views.html.portal.orders.render(user,cart,orders,pageNumber,categories));
    }
}

