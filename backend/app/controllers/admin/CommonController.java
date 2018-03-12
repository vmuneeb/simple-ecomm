package controllers.admin;

import actions.ActionAuthenticator;
import com.google.inject.Inject;
import controllers.api.ProductController;
import dto.ProductDto;
import models.banner.Banner;
import models.product.Brand;
import models.product.Category;
import models.product.Product;
import models.product.SubCategory;
import models.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by muneeb on 26/03/17.
 */
public class CommonController extends Controller {


    @Inject
    ProductController productController;



    private static final Logger LOG = LoggerFactory.getLogger(AdminController.class);

    @Security.Authenticated(ActionAuthenticator.class)
    public Result banners() {
        String message = null;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Banner> banners = Banner.find.all();
        return Results.ok(views.html.admin.banner.render(user,banners,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addBanner() throws IOException{
        String message = productController.addBanner(request());
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Banner> banners = Banner.find.all();
        return Results.ok(views.html.admin.banner.render(user,banners,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result brands() {
        String message = null;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Brand> brands = Brand.find.all();
        return Results.ok(views.html.admin.brand.render(user,brands,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addBrand() throws IOException{
        String message = productController.addBrand(request());
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Brand> brands = Brand.find.all();
        return Results.ok(views.html.admin.brand.render(user,brands,message));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result category() {
        String message = null;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Category> categories = Category.find.all();
        return Results.ok(views.html.admin.category.render(user,categories,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addCategory() throws IOException{
        String message = productController.addCategory(request());
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Category> categories = Category.find.all();
        return Results.ok(views.html.admin.category.render(user,categories,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result subCategory(long categoryId) {
        String message = null;
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.where().eq("category_id",categoryId).findList();
        return Results.ok(views.html.admin.subcategory.render(user,categories,subCategories,brands,categoryId,message));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result addSubCategory(long categoryId) throws IOException{
        String message = productController.addSubCategory(request(),categoryId);
        User user = User.find.where().eq("id",Integer.parseInt(session("admin"))).findUnique();
        List<Category> categories = Category.find.all();
        List<Brand> brands = Brand.find.all();
        List<SubCategory> subCategories = SubCategory.find.where().eq("category_id",categoryId).findList();
        return Results.ok(views.html.admin.subcategory.render(user,categories,subCategories,brands,categoryId,message));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteBanner(String id) throws IOException {
        LOG.info("In deleteBrand");
        Banner banner = Banner.find.where().eq("id",id).findUnique();
        if(banner == null) {
            return notFound();
        }
        banner.delete();
        return redirect("/admin/banners");
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteSubCategory(String subCategoryId) throws IOException{
        LOG.info("In deleteSubCategory for id {}",subCategoryId);
        SubCategory subCategory = SubCategory.find.where().eq("id",subCategoryId).findUnique();
        if(subCategory == null) {
            return notFound();
        }
        long categoryId = subCategory.category.id;
        subCategory.delete();
        return redirect("/admin/category/"+categoryId);
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteProduct(String id) throws IOException{
        LOG.info("In deleteProduct");
        Product product = Product.find.where().eq("id",id).findUnique();
        if(product == null) {
            return notFound();
        }
        product.delete();
        return redirect("/admin/category/"+product.category);
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteBrand(String id) throws IOException{
        LOG.info("In deleteBrand");
        Brand brand = Brand.find.where().eq("id",id).findUnique();
        if(brand == null) {
            return notFound();
        }
        brand.delete();
        return redirect("/admin/brands");
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result deleteCategory(String id) throws IOException{
        LOG.info("In deleteCategory");
        Category category = Category.find.where().eq("id",id).findUnique();
        if(category == null) {
            return notFound();
        } else{
            List<Product> products = Product.find.where().eq("category_id",category.id).findList();
            products.forEach(product -> {
                product.delete();
            });
        }
        category.delete();
        return redirect("/admin/category");
    }
}
