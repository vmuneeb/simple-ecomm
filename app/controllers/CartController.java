package controllers;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import dto.CartDto;
import models.cart.Cart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.Map;

/**
 * Created by muneeb on 07/01/17.
 */
public class CartController extends Controller {

    @Inject
    FormFactory formFactory;

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    public Result addToCart() {
        Form<CartDto> form = formFactory.form(CartDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }
        CartDto cartDto = form.get();
        Cart cart = new Cart();
        return Results.ok(Json.toJson(cart));
    }

//    public Result getCart() throws Exception{
//        Http.Request req = Http.Context.current().request();
//        Map<String, String[]> headerMap = req.headers();
//        for (String headerKey : headerMap.keySet()) {
//            LOG.info("Key: " + headerKey + " - Value: " + headerMap.get(headerKey));
//        }
//        ObjectMapper mapper = new ObjectMapper();
//        JsonNode actualObj = mapper.readTree("{\n" +
//                "  \"id\": 14,\n" +
//                "  \"product_count\": 2,\n" +
//                "  \"total_price\": 300,\n" +
//                "  \"total_price_formatted\": 300,\n" +
//                "  \"currency\": \"USD\",\n" +
//                "  \"discounts\": [\n" +
//
//                "  ],\n" +
//                "  \"items\": [\n" +
//                "    {\n" +
//                "      \"id\": 1,\n" +
//                "      \"remote_id\": 605967,\n" +
//                "      \"quantity\": 1,\n" +
//                "      \"total_item_price\": 200,\n" +
//                "      \"total_item_price_formatted\": 200,\n" +
//                "  \"variant\": \n" +
//                "    {\n" +
//                "      \"id\": 6005937,\n" +
//                "      \"remote_id\": 605937,\n" +
//                "      \"product_id\": 600937,\n" +
//                "      \"name\": \"shoe\",\n" +
//                "      \"price\": 300,\n" +
//                "      \"price_formatted\": 300,\n" +
//                "      \"category\": 100,\n" +
//                "      \"currency\": \"USD\",\n" +
//                "      \"code\": \"assdf\",\n" +
//                "      \"description\": \"Description here\",\n" +
//                "      \"main_image\": \"http://img.bfashion.com/products/big/2478725b91a8a21fab6ab87cf12634b3e71a1b75.jpg\"\n" +
//                "    }\n" +
//                "    }, {" +
//                "      \"id\": 2,\n" +
//                "      \"remote_id\": 695967,\n" +
//                "      \"quantity\": 1,\n" +
//                "      \"total_item_price\": 200,\n" +
//                "      \"total_item_price_formatted\": 200,\n" +
//                "  \"variant\": \n" +
//                "    {\n" +
//                "      \"id\": 6005937,\n" +
//                "      \"remote_id\": 605937,\n" +
//                "      \"product_id\": 600937,\n" +
//                "      \"name\": \"hat\",\n" +
//                "      \"price\": 250,\n" +
//                "      \"price_formatted\": 250,\n" +
//                "      \"category\": 100,\n" +
//                "      \"currency\": \"USD\",\n" +
//                "      \"code\": \"assdf\",\n" +
//                "      \"description\": \"Description here\",\n" +
//                "      \"main_image\": \"http://img.bfashion.com/products/big/2478725b91a8a21fab6ab87cf12634b3e71a1b75.jpg\"\n" +
//                "    }\n" +
//                "    }\n" +
//                "  ]\n" +
//                "}");
//        return Results.ok(actualObj);
//    }

    public Result getCart() throws Exception{
        Http.Request req = Http.Context.current().request();
        Map<String, String[]> headerMap = req.headers();
        for (String headerKey : headerMap.keySet()) {
            LOG.info("Key: " + headerKey + " - Value: " + headerMap.get(headerKey));
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree("{\n" +
                "  \"id\": 14,\n" +
                "  \"product_count\": 2,\n" +
                "  \"total_price\": 300,\n" +
                "  \"total_price_formatted\": 300,\n" +
                "  \"currency\": \"USD\",\n" +
                "  \"items\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"remote_id\": 605967,\n" +
                "      \"quantity\": 5,\n" +
                "      \"total_item_price\": 200,\n" +
                "      \"total_item_price_formatted\": 200,\n" +
                "  \"variant\": \n" +
                "    {\n" +
                "      \"id\": 6005937,\n" +
                "      \"remote_id\": 605937,\n" +
                "      \"product_id\": 600937,\n" +
                "      \"name\": \"hat\",\n" +
                "      \"price\": 250,\n" +
                "      \"price_formatted\": 250,\n" +
                "      \"category\": 100,\n" +
                "      \"currency\": \"USD\",\n" +
                "      \"code\": \"assdf\",\n" +
                "      \"description\": \"Description here\",\n" +
                "      \"main_image\": \"http://img.bfashion.com/products/big/2478725b91a8a21fab6ab87cf12634b3e71a1b75.jpg\"\n" +
                "    }\n" +
                "   }\n" +
                "  ]\n" +
                "}");
        return Results.ok(actualObj);
    }

    public Result getCartInfo() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree("{\n" +
                "\t\"product_count\": 2,\n" +
                "\t\"total_price\": 200,\n" +
                "\t\"total_price_formatted\": 400\n" +
                "\n" +
                "}");
        return Results.ok(actualObj);
    }
}
