package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Shop;
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
        List<Shop> shopList = Shop.find.where().findList();
        ObjectNode res = new Json().newObject();
        res.put("records",Json.toJson(shopList));
        res.set("metadata",null);
        return Results.ok(res);
    }

    public Result navigation() {
        List<Shop> shopList = Shop.find.where().findList();
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
        ObjectNode resultJson = Json.newObject();
        List<ObjectNode> listNode = new ArrayList<>();
        ObjectNode res = new Json().newObject();
        res.put("id","1");
        res.put("target","list:67049");
        res.put("name","Shoes");
        res.put("type","category");
        res.put("image_url","http://77.93.198.186/u/2016/05/03/1462284245-9.jpg");
        listNode.add(res);

        res = new Json().newObject();
        res.put("id","2");
        res.put("target","list:67048");
        res.put("name","T-shirts");
        res.put("type","category");
        res.put("image_url","http://77.93.198.186/u/2016/05/03/1462284283-74.jpg");
        listNode.add(res);

        res = new Json().newObject();
        res.put("id","3");
        res.put("target","list:67047");
        res.put("name","Skirts");
        res.put("image_url","http://77.93.198.186/u/2016/05/03/1462284326-39.jpg");
        listNode.add(res);

        resultJson.putArray("records").addAll(listNode);

        return Results.ok(resultJson);
    }
}
