package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.product.Product;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muneeb on 07/01/17.
 */
public class ProductController extends Controller{

    public Result getProducts(String category,String sort) throws IOException {
        ObjectNode productsJson = Json.newObject();
        List<Product> products = Product.find.where().eq("category_id",category).findList();
        List<ObjectNode> listNode = new ArrayList<>();
        products.forEach(product -> {
            ObjectNode res = new Json().newObject();
            res.put("id",product.id);
            res.put("name",product.name);
            res.put("description",product.description);
            res.put("price",product.price);
            res.put("price_formatted",product.price);
            res.put("discount_price",product.discountPrice);
            res.put("discount_price_formatted",product.price+"");
            res.put("main_image",product.mainImage);
            listNode.add(res);
        });
        productsJson.set("records",Json.toJson(listNode));
        return Results.ok(productsJson);
    }


    public Result getProduct(String id) throws IOException{
        Product product = Product.find.where().eq("id",1).findUnique();
        ObjectNode res = new Json().newObject();
        res.put("id",product.id);
        res.put("name",product.name);
        res.put("description",product.description);
        res.put("price",product.price);
        res.put("price_formatted",product.price);
        res.put("discount_price",product.discountPrice);
        res.put("discount_price_formatted",product.price+"");
        res.put("main_image",product.mainImage);
        return Results.ok(res);
    }
}
