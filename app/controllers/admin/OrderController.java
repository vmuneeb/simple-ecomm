package controllers.admin;

import actions.ActionAuthenticator;
import actions.SessionAuthenticator;
import com.avaje.ebean.Expr;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.order.Order;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.Security;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by muneeb on 06/02/17.
 */
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    Config config = ConfigFactory.load();
    private int order_limit = config.getInt("order.count.per.page");

    private static String ORDER_FIELD = "updated_time";

    @Security.Authenticated(ActionAuthenticator.class)
    public Result getOrders(int pageNumber,String search) {
        LOG.info("In getOrders");
        List<Order> orders;
        if(search == null || TextUtils.isEmpty(search))
            orders = Order.find.where().setMaxRows(order_limit).setFirstRow(pageNumber*order_limit).orderBy(ORDER_FIELD+" desc").findList();
        else
            orders = Order.find.where().like("name",search).setMaxRows(order_limit).setFirstRow(pageNumber*order_limit).findList();
        return Results.ok(Json.toJson(orders));
    }

    @Security.Authenticated(ActionAuthenticator.class)
    public Order getOrder(String userId,String orderId) {
        LOG.info("In getOrders");
        LOG.info("User is {}",userId);
        Order order = Order.find.where().eq("user_id",userId).eq("id",orderId).findUnique();
        return order;
    }
}
