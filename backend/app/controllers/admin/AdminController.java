package controllers.admin;


import actions.ActionAuthenticator;
import com.avaje.ebean.Expr;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import controllers.api.ProductController;
import dto.LoginDto;
import models.order.OrderStatus;
import models.product.Brand;
import models.product.SubCategory;
import models.user.User;
import models.user.UserType;
import models.order.Order;
import models.product.Category;
import models.product.Product;
import org.apache.http.util.TextUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;
import play.twirl.api.Html;
import plugins.S3Plugin;

import java.io.IOException;
import java.util.List;

/**
 * Created by muneeb on 30/01/17.
 */
public class AdminController extends Controller{

    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Inject
    FormFactory formFactory;

    @Inject
    ProductController productController;

    @Inject
    S3Plugin s3Plugin;

    Config config = ConfigFactory.load();
    private int order_limit = config.getInt("order.count.per.page");




    private int product_limit = config.getInt("product.count.per.page");

    private static String ORDER_FIELD = "updated_time";


    public Result index() {
        String user = session("admin");
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
            session("admin",Long.toString(user.id));
            LOG.info("login success");
            return redirect("/admin/home");
        }
        LOG.info("login failed for type {}", UserType.ADMIN.ordinal());
        return Results.ok(views.html.admin.index.render());
    }

    public Result logout() {
        session().clear();
        return Results.ok(views.html.admin.index.render());
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result home(String search) {
        int pageNumber = 0;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Order> orders;
        LOG.info("search string is {}",search);
        if(search == null || TextUtils.isEmpty(search))
            orders = Order.find.where().setMaxRows(order_limit).setFirstRow(pageNumber*order_limit).orderBy(ORDER_FIELD+" desc").findList();
        else
            orders = Order.find.where().like("name",search).setMaxRows(order_limit).setFirstRow(pageNumber*order_limit).findList();
        return Results.ok(views.html.admin.home.render(user,orders,search));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result order(String id) {
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        if(user == null)
        {
            return Results.forbidden();
        }
        Order order = Order.find.where().eq("user_id",user.id).eq("id",id).findUnique();
        return Results.ok(views.html.admin.order.render(user,order,OrderStatus.values()));
    }



    @Security.Authenticated(ActionAuthenticator.class)
    public Result products(int pageNumber,String search) {
        LOG.info("Get products from admin");
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Product> products = null;

        if(search == null )
            products =  Product.find.where().setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        else
        {
            LOG.info("Search string is {}",search);
            products =  Product.find.where().or(Expr.like("name","%"+search+"%"),Expr.like("description","%"+search+"%")).setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        }

        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        Html chartData = new Html(Json.toJson(subCategories).toString());
        return Results.ok(views.html.admin.products.render(user,categories,subCategories,brands,products,pageNumber,chartData,null,search));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result updateProduct(long id) {
        String message = productController.updateProduct(request(),id);
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        Product product = Product.find.where().eq("id",id).findUnique();
        if(product == null) {
            return notFound();
        }
        LOG.info(message);
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        return Results.ok(views.html.admin.product.render(user,categories,subCategories,brands,product,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addProduct() throws IOException{
        int pageNumber = 0;
        String message = productController.addProduct(request());
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Product> products = Product.find.where().setMaxRows(10).setFirstRow(pageNumber*10).findList();
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        Html chartData = new Html(Json.toJson(subCategories).toString());
        return Results.ok(views.html.admin.products.render(user,categories,subCategories,brands,products,pageNumber,chartData,message,null));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result product(int id) {
        String message = null;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        Product product = Product.find.where().eq("id",id).findUnique();
        if(product == null) {
            return notFound();
        }
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.all();
        return Results.ok(views.html.admin.product.render(user,categories,subCategories,brands,product,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result users(int pageNumber) {
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<User> users = User.find.setMaxRows(10).setFirstRow(pageNumber*10).findList();
        return Results.ok(views.html.admin.users.render(user,users));
    }
}
