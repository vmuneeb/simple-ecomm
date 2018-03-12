package controllers.api;


import com.avaje.ebean.Expr;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dto.*;
import models.banner.Banner;
import models.product.Brand;
import models.product.Category;
import models.product.Product;
import models.product.SubCategory;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by muneeb on 07/01/17.
 */
public class ProductController extends Controller{

    @Inject
    FormFactory formFactory;

    Config config = ConfigFactory.load();

    private int product_limit = config.getInt("product.count.per.page");

    private static String ORDER_FIELD = "updated_time";




    private static final Logger LOG = LoggerFactory.getLogger(ProductController.class);

    public List<Product> getProducts(String category,String subCategory,String brand,String search,int pageNumber){
        List<Product> products ;
        if(search != null) {
            LOG.info("Search string is {}",search);
            products = Product.find.where().or(Expr.like("name","%"+search+"%"),Expr.like("description","%"+search+"%")).setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        }else if(category != null)
            products = Product.find.where().eq("category_id",category).setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        else if(brand != null)
            products = Product.find.where().eq("brand_id",brand).setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        else if(subCategory != null)
            products = Product.find.where().eq("sub_category_id",subCategory).setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        else
            products = Product.find.where().setMaxRows(product_limit).setFirstRow(pageNumber*product_limit).orderBy(ORDER_FIELD+" desc").findList();
        return products;
    }

    public  Result getProductsResult(String category,String subCategory,String brand,String search,int pageNumber){
        List<Product> products = getProducts(category,subCategory,brand,search,pageNumber);
        ObjectNode result = Json.newObject();
        result.set("records",Json.toJson(products));
        return Results.ok(result);
    }


    public Result getProduct(String id) throws IOException{
        Product product = Product.find.where().eq("id",id).findUnique();
        ObjectNode res = new Json().newObject();
        res.put("id",product.id);
        res.put("name",product.name);
        res.put("description",product.description);
        res.put("price",product.price);
        res.put("price_formatted",product.price);
        res.put("discount_price",product.discountPrice);
        res.put("discount_price_formatted",product.price+"");
        res.put("main_image",product.getMainImage());
        return Results.ok(res);
    }


    public String addProduct(Http.Request request) throws IOException{
        LOG.info("In addProduct");
        String message  = "success";

        Form<ProductDto> form = formFactory.form(ProductDto.class).bindFromRequest(request);

        if (form.hasErrors()) {
            message = "Please enter valid details";
            LOG.info("Form has error {}",form.errors().toString());
            return message;
        }

        ProductDto productDto = form.get();
        String fileName = "/tmp/"+ RandomStringUtils.randomAlphanumeric(8)+".png";

        String dataUrl = productDto.image;
        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        LOG.info("Saving to {}",fileName);


        FileUtils.writeByteArrayToFile(new File(fileName),imageData);

        File savedFile = new File(fileName);


        Product product = new Product();
        Brand brand = Brand.find.where().eq("id",productDto.brand).findUnique();
        Category category = Category.find.where().eq("id",productDto.category).findUnique();
        SubCategory subCategory = SubCategory.find.where().eq("id",productDto.subCategory).findUnique();
        if(category == null || subCategory == null) {
            message = "Category or subCategory empty";
            LOG.info(message);
            return message;
        }

        product.brand = brand;
        product.category = category;
        product.subCategory = subCategory;
        product.name = productDto.name;
        product.description = productDto.desc;
        product.setImage(savedFile);
        product.discountPrice = productDto.discountPrice;
        product.quantity = productDto.quantity;
        product.formattedPrice =productDto.price+"";
        product.discountPriceFormatted = productDto.discountPrice+"";
        product.price = productDto.price;
        product.featured = productDto.featured;
        product.save();
        return message;
    }


    public String updateProduct(Http.Request request, long id){
        LOG.info("In addProduct");
        String message = "success";

        Form<UpdateProductDto> form = formFactory.form(UpdateProductDto.class).bindFromRequest(request);

        if (form.hasErrors()) {
            message = "Please enter valid details";
            LOG.info("Form has error {}",form.errors().toString());
            return message;
        }

        UpdateProductDto productDto = form.get();

        Product product = Product.find.where().eq("id",id).findUnique();
        Brand brand = Brand.find.where().eq("id",productDto.brand).findUnique();
        Category category = Category.find.where().eq("id",productDto.category).findUnique();
        SubCategory subCategory = SubCategory.find.where().eq("id",productDto.subCategory).findUnique();
        if(brand == null || category == null || subCategory == null) {
            message = "Brand or category or subCategory empty";
            LOG.info(message);
            return message;
        }

        product.brand = brand;
        product.category = category;
        product.subCategory = subCategory;
        product.name = productDto.name;
        product.description = productDto.desc;
        product.discountPrice = productDto.discountPrice;
        product.quantity = productDto.quantity;
        product.formattedPrice = productDto.price+"";
        product.discountPriceFormatted = productDto.discountPrice+"";
        product.price = productDto.price;
        product.featured = productDto.featured;
        product.save();
        return message;
    }


    public String addBanner(Http.Request request) throws IOException{
        String message  = "success";
        Form<BannerDto> form = formFactory.form(BannerDto.class).bindFromRequest(request);
        if (form.hasErrors()) {
            message = "Please enter valid details";
            return message;
        }

        BannerDto bannerDto = form.get();
        String fileName = "/tmp/"+ RandomStringUtils.randomAlphanumeric(8)+".png";

        String dataUrl = bannerDto.image;
        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        LOG.info("Saving to {}",fileName);

        FileUtils.writeByteArrayToFile(new File(fileName),imageData);
        File savedFile = new File(fileName);

        Banner  banner= new Banner();
        banner.name = bannerDto.name;
        banner.setImage(savedFile);
        banner.save();
        return message;
    }



    public String addBrand(Http.Request request) throws IOException{
        LOG.info("In addBrand ");
        String message  = "success";
        Form<BrandDto> form = formFactory.form(BrandDto.class).bindFromRequest(request);

        if (form.hasErrors()) {
            message = "Please enter valid details";
            return message;
        }

        BrandDto brandDto = form.get();

        String fileName = "/tmp/"+ RandomStringUtils.randomAlphanumeric(8)+".png";

        String dataUrl = brandDto.image;
        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        LOG.info("Saving to {}",fileName);

        FileUtils.writeByteArrayToFile(new File(fileName),imageData);

        File savedFile = new File(fileName);
        Brand  brand= new Brand();
        brand.name = brandDto.name;
        brand.setImage(savedFile);
        brand.save();
        return message;
    }

    public String addCategory(Http.Request request) throws IOException{
        LOG.info("In addCategory ");
        String message  = "success";

        Form<CategoryDto> form = formFactory.form(CategoryDto.class).bindFromRequest(request);

        if (form.hasErrors()) {
            message = "Please enter valid details";
            return message;
        }

        CategoryDto categoryDto = form.get();

        String fileName = "/tmp/"+ RandomStringUtils.randomAlphanumeric(8)+".png";

        String dataUrl = categoryDto.image;
        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        LOG.info("Saving to {}",fileName);

        FileUtils.writeByteArrayToFile(new File(fileName),imageData);

        File savedFile = new File(fileName);


        Category  category= new Category();
        category.name = categoryDto.name;
        category.setImage(savedFile);
        category.save();
        return message;
    }

    public String addSubCategory(Http.Request request,Long categoryId) throws IOException{
        LOG.info("In addCategory ");
        String message = "success";

        Form<SubCategoryDto> form = formFactory.form(SubCategoryDto.class).bindFromRequest(request);

        if (form.hasErrors()) {
            message = "Please enter valid details";
            return message;
        }

        SubCategoryDto subCategoryDto = form.get();

        String fileName = "/tmp/"+ RandomStringUtils.randomAlphanumeric(8)+".png";

        String dataUrl = subCategoryDto.image;
        String encodingPrefix = "base64,";
        int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
        byte[] imageData = Base64.decodeBase64(dataUrl.substring(contentStartIndex));

        LOG.info("Saving to {}",fileName);

        FileUtils.writeByteArrayToFile(new File(fileName),imageData);

        File savedFile = new File(fileName);


        Category category = Category.find.where().eq("id",categoryId).findUnique();

        SubCategory subCategory= new SubCategory();
        subCategory.name = subCategoryDto.name;
        subCategory.category = category;
        subCategory.setImage(savedFile);
        subCategory.save();
        return message;
    }
}
