package controllers.api;

import Util.MailUtility;
import Util.PdfUtility;
import Util.Utility;
import actions.ActionAuthenticator;
import actions.SessionAuthenticator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import dto.OrderDto;
import dto.OrderUpdateDto;
import models.order.OrderStatus;
import models.product.Product;
import models.user.User;
import models.cart.Cart;
import models.cart.CartProduct;
import models.order.Order;
import models.order.OrderProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by muneeb on 07/01/17.
 */
public class OrderController extends Controller {

    @Inject
    FormFactory formFactory;

    @Inject
    MailUtility mailUtility;


    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Security.Authenticated(SessionAuthenticator.class)
    public Result createOrder() {

        LOG.info("Request is {}",request().body().asText());

        Form<OrderDto> form = formFactory.form(OrderDto.class).bindFromRequest();
        if (form.hasErrors()) {
            LOG.info(form.errors().toString());
            return Results.badRequest("Creating order failed. Please give correct values");
        }

        OrderDto orderDto = form.get();

        LOG.info("OrderDto is {}",orderDto);

        String userId = request().username();

        LOG.info("User id is {}",userId);

        User user = User.find.where().eq("id",Integer.parseInt(userId)).findUnique();

        LOG.info("User  is {}",Json.toJson(user));

        Cart cart = Cart.find.where().eq("user_id",Integer.parseInt(userId)).findUnique();

        LOG.info("Cart  is {}",Json.toJson(cart));

        if(user == null || cart ==null || cart.items.size() <= 0) {
            return Results.badRequest("No product in cart");
        }

        Order order = new Order();
        List<OrderProduct> orderProducts = new LinkedList<>();

        double totalPice = 0;
        int productCount = 0;
        Product product;
        for (CartProduct item : cart.items) {
            product = Product.find.where().eq("id",item.productId).findUnique();
            if(product == null) {
                return notFound("No such product");
            }
            if(product.quantity > 0) {
                product.quantity = product.quantity-item.quantity;
            } else {
                LOG.info("Out of stock");
                return Results.badRequest("Some products are out of stock");
            }
            product.save();
            orderProducts.add(OrderProduct.fromCartProduct(item,order));
            totalPice=totalPice+item.discountPrice*item.quantity;
            productCount+=item.quantity;
        }

        LOG.info("Name is {}",orderDto.name);

        order.name =orderDto.name;
        order.city =orderDto.city;
        order.email =orderDto.email;

        order.building =orderDto.building;
        order.street = orderDto.street;
        order.area = orderDto.area;
        order.city = orderDto.city;
        order.phone =orderDto.phone;

        user.name = order.name;
        user.building = order.building;
        user.street = order.street;
        user.area = order.area;
        user.city = order.city;
        user.phone = order.phone;


        order.status = OrderStatus.CREATED;
        order.totalPrice = totalPice;
        order.totalFormatted = Utility.getFormattedPrice(totalPice);

        order.productCount =productCount;
        order.items = orderProducts;
        order.productCount = orderProducts.size();
        order.user = user;

        cart.delete();
        order.save();
        user.save();

        return Results.ok(Json.toJson(cart));
    }


    @Security.Authenticated(ActionAuthenticator.class)
    public Result updateOrder(Long orderId) {
        LOG.info("In updateOrder");
        Order order = Order.find.where().eq("id",orderId).findUnique();
        Form<OrderUpdateDto> form = formFactory.form(OrderUpdateDto.class).bindFromRequest();
        if (form.hasErrors()) {
            LOG.info(form.errors().toString());
            return Results.badRequest();
        }
        OrderUpdateDto orderUpdateDto = form.get();
        if(order != null) {
            order.status = OrderStatus.valueOf(orderUpdateDto.status);
            order.save();
        }
        if(order.status.equals(OrderStatus.CONFIRMED)) {
            mailUtility.sendInvoice(order);
        }
        return redirect("/admin/orders/"+orderId);
    }

    @Security.Authenticated(SessionAuthenticator.class)
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

    @Security.Authenticated(SessionAuthenticator.class)
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

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getInvoice(String orderId) throws FileNotFoundException {
        LOG.info("In getOrders");
        Order order = Order.find.where().eq("id",orderId).findUnique();
       if(order == null) return notFound();
        response().setHeader("Content-disposition","attachment; filename=invoice.pdf");
        return Results.ok(new FileInputStream(PdfUtility.getInvoice(order))).as("application/pdf");
    }
}

