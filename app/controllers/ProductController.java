package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import java.io.IOException;

/**
 * Created by muneeb on 07/01/17.
 */
public class ProductController extends Controller{

    public Result getProducts() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree("{\n" +
                "  \"metadata\": {\n" +
                "    \"links\": {\n" +
                "      \"first\": \"http://77.93.198.186/v1.2/21/products\",\n" +
                "      \"last\": \"http://77.93.198.186/v1.2/21/products?offset=6360\",\n" +
                "      \"prev\": null,\n" +
                "      \"next\": \"http://77.93.198.186/v1.2/21/products?offset=20\",\n" +
                "      \"self\": \"http://77.93.198.186/v1.2/21/products\"\n" +
                "    },\n" +
                "    \"sorting\": \"newest\",\n" +
                "    \"records_count\": 6365\n" +
                "  },\n" +
                "  \"records\": [\n" +
                "    {\n" +
                "      \"id\": 5999542,\n" +
                "      \"remote_id\": 3155,\n" +
                "      \"url\": \"http://cz.bfashion.com/na-podpatku/jennika-lodi%C4%8Dky-jennika-3155\",\n" +
                "      \"name\": \"Pumps\",\n" +
                "      \"price\": 295,\n" +
                "      \"price_formatted\": \"$295.00\",\n" +
                "      \"category\": 67010,\n" +
                "      \"brand\": \"JENNIKA\",\n" +
                "      \"discount_price\": 119,\n" +
                "      \"discount_price_formatted\": \"$119.00\",\n" +
                "      \"currency\": \"USD\",\n" +
                "      \"code\": \"D003155\",\n" +
                "      \"description\": \"You can add a description of the product to the feed and it will appear exactly here.\",\n" +
                "      \"main_image\": \"http://img.bfashion.com/products/presentation/1023dc979f5e98c34c202a6f82a3af3ab6feeda4.jpg\",\n" +
                "      \"main_image_high_res\": \"http://img.bfashion.com/products/big/1023dc979f5e98c34c202a6f82a3af3ab6feeda4.jpg\",\n" +
                "      \"images\": [],\n" +
                "      \"variants\": []\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        return Results.ok(actualObj);
    }


    public Result getProduct(String id) throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree("{\n" +
                "  \"id\": 5999544,\n" +
                "  \"name\": \"Short plaid jacket ladies Sarah\",\n" +
                "  \"price\": 3800,\n" +
                "  \"price_formatted\": \"$3,800.00\",\n" +
                "  \"category\": 67012,\n" +
                "  \"discount_price\": 3800,\n" +
                "  \"discount_price_formatted\": \"$3,800.00\",\n" +
                "  \"currency\": \"USD\",\n" +
                "  \"description\": \"You can add a description of the product to the feed and it will appear exactly here.\",\n" +
                "  \"variants\": [\n" +
                "    {\n" +
                "      \"id\": 6005937,\n" +
                "      \"color\": {\n" +

                "      },\n" +
                "      \"size\": {\n" +
                "        \"id\": 36779,\n" +
                "        \"remote_id\": 4,\n" +
                "        \"value\": \"L\"\n" +
                "      },\n" +
                "      \"images\": [\n" +
                "        \"http://img.bfashion.com/products/big/2478725b91a8a21fab6ab87cf12634b3e71a1b75.jpg\"\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}");
        return Results.ok(actualObj);
    }

    public Result getProductsForCategory(String categoryId) {
        return Results.ok();
    }

    public Result getProductsForBrand(String brandId) {
        return Results.ok();
    }
}
