package controllers.admin;

import actions.SessionAuthenticator;
import models.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Security;

import java.util.List;

/**
 * Created by muneeb on 06/02/17.
 */
public class OrderController {

    private static final Logger LOG = LoggerFactory.getLogger(OrderController.class);

    @Security.Authenticated(SessionAuthenticator.class)
    public List<Order> getOrders(String userId) {
        LOG.info("In getOrders");
        List<Order> orders = Order.find.where().eq("user_id",userId).findList();
        return orders;
    }

    public Order getOrder(String userId,String orderId) {
        LOG.info("In getOrders");
        LOG.info("User is {}",userId);
        Order order = Order.find.where().eq("user_id",userId).eq("id",orderId).findUnique();
        return order;
    }
}
