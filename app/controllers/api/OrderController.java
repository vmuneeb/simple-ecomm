package controllers.api;

import Util.Utility;
import actions.ActionAuthenticator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dto.OrderDto;
import dto.SignupDto;
import models.User;
import models.cart.Cart;
import models.cart.CartProduct;
import models.order.Order;
import models.order.OrderProduct;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * Created by muneeb on 07/01/17.
 */
public class OrderController extends Controller {

    @Inject
    FormFactory formFactory;

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Security.Authenticated(ActionAuthenticator.class)
    public Result createOrder() {

        Form<OrderDto> form = formFactory.form(OrderDto.class).bindFromRequest();

        if (form.hasErrors()) {
            return Results.badRequest();
        }

        OrderDto orderDto = form.get();

        LOG.info("OrderDto is {}",orderDto);

        String userId = request().username();

        User user = User.find.where().eq("id",userId).findUnique();

        Cart cart = Cart.find.where().eq("user_id",userId).findUnique();

        if(user == null || cart ==null || cart.items.size() <= 0) {
            return Results.badRequest("No product in cart");
        }

        Order order = new Order();
        List<OrderProduct> orderProducts = new LinkedList<>();

        double totalPice = 0;
        int productCount = 0;
        for (CartProduct item : cart.items) {
            orderProducts.add(OrderProduct.fromCartProduct(item,order));
            totalPice=totalPice+item.price;
            productCount+=item.quantity;
        }

        LOG.info("Name is {}",orderDto.getName());

        order.name =orderDto.getName();
        order.city =orderDto.getCity();
        order.email =orderDto.getEmail();
        order.houseNumber =orderDto.getHouse_number();
        order.phone =orderDto.getPhone();
        order.zip = orderDto.getZip();
        order.shippingName = orderDto.getName();
        order.status = "CREATED";
        order.street = orderDto.getStreet();
        order.totalPrice = totalPice;
        order.totalFormatted = Utility.getFormattedPrice(totalPice);

        order.productCount =productCount;
        order.items = orderProducts;
        order.productCount = orderProducts.size();
        order.user = user;

        cart.delete();
        order.save();
        return Results.ok(Json.toJson(order));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getOrders() {
        LOG.info("In getOrders");
        String userId = request().username();
        LOG.info("User is {}",userId);
        List<Order> orders = Order.find.where().eq("user_id",userId).findList();
        ObjectNode orderJson = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mapper.setDateFormat(outputFormat);
        Json.setObjectMapper(mapper);
        orderJson.set("records",Json.toJson(orders));
        LOG.info("order json {}",orderJson);
        return Results.ok(Json.toJson(orderJson));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getOrder(String id) {
        LOG.info("In getOrders");
        String userId = request().username();
        LOG.info("User is {}",userId);
        Order order = Order.find.where().eq("user_id",userId).eq("id",id).findUnique();
        ObjectNode orderJson = Json.newObject();
        ObjectMapper mapper = new ObjectMapper();
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        mapper.setDateFormat(outputFormat);
        Json.setObjectMapper(mapper);
        LOG.info("order json {}",order);
        return Results.ok(Json.toJson(order));
    }
}

