package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.product.Category;
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
        List<ObjectNode> listNode = new ArrayList<>();
        ObjectNode res = new Json().newObject();
        res.put("id","1");
        res.put("original_id","1234");
        res.put("name","men");
        res.put("type","category");
        res.put("weight","0");
        res.put("graph_id","961");
        listNode.add(res);

        res = new Json().newObject();
        res.put("id","2");
        res.put("original_id","1254");
        res.put("name","women");
        res.put("type","category");
        res.put("weight","0");
        res.put("graph_id","962");
        listNode.add(res);

        res = new Json().newObject();
        res.put("id","2");
        res.put("original_id","1254");
        res.put("name","kids");
        res.put("type","category");
        res.put("weight","0");
        res.put("graph_id","962");
        listNode.add(res);

        resultJson.putArray("navigation").addAll(listNode);

        listNode = new ArrayList<>();
        res = new Json().newObject();
        res.put("id","3");
        res.put("name","pages");
        listNode.add(res);


        resultJson.putArray("pages").addAll(listNode);
        return Results.ok(resultJson);
    }


    public Result banners() {
        List<Category> categories = Category.find.all();
        ObjectNode resultJson = Json.newObject();
        List<ObjectNode> listNode = new ArrayList<>();

        categories.forEach(category -> {
            ObjectNode res = new Json().newObject();
            res.put("id",category.categoryId);
            res.put("target","list:"+category.categoryId);
            res.put("name",category.name);
            res.put("type","category");
            res.put("image_url",category.image);
            listNode.add(res);
        });

        resultJson.putArray("records").addAll(listNode);

        return Results.ok(resultJson);
    }
}
