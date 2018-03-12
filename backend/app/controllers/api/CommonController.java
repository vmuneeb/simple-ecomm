package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import models.banner.Banner;
import models.product.Category;
import models.product.SubCategory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muneeb on 07/01/17.
 */
public class CommonController  extends Controller {
    public Result getShops() {
        ObjectNode res = new Json().newObject();
        res.put("records",Json.toJson(null));
        res.set("metadata",null);
        return Results.ok(res);
    }


    public Result navigation() {
        ObjectNode resultJson = Json.newObject();
        List<SubCategory> categories = SubCategory.find.all();

        List<JsonNode> listNode = new ArrayList<>();
        categories.forEach(category -> {
            listNode.add(Json.toJson(category));
        });
        resultJson.putArray("navigation").addAll(listNode);
        return Results.ok(resultJson);
    }


    public Result banners() {
        List<SubCategory> categories = SubCategory.find.all();
        ObjectNode resultJson = Json.newObject();
        List<ObjectNode> listNode = new ArrayList<>();

        categories.forEach(category -> {
            ObjectNode res = new Json().newObject();
            res.put("id",category.id);
            res.put("target","list:"+category.id);
            res.put("name",category.name);
            res.put("type","category");
            res.put("image_url",category.getImageUrl());
            listNode.add(res);
        });

        resultJson.putArray("records").addAll(listNode);


        return Results.ok(resultJson);
    }
}
