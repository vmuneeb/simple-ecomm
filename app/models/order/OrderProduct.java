package models.order;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.cart.CartProduct;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by muneeb on 22/01/17.
 */

@Entity
public class OrderProduct extends Model{

    @Id
    public long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Order order;

    public long productId;

    public String name;

    public double price;

    public double discountPrice;

    @JsonProperty("price_formatted")
    public String formattedPrice;

    @JsonProperty("discount_price_formatted")
    public String discountPriceFormatted;

    public int quantity;

    @JsonProperty("main_image")
    public String mainImage;

    public static OrderProduct fromCartProduct(CartProduct cartProduct,Order order) {
        OrderProduct orderProduct = new OrderProduct();
        orderProduct.order = order;
        orderProduct.name=cartProduct.name;
        orderProduct.mainImage=cartProduct.mainImage;
        orderProduct.price= cartProduct.price;
        orderProduct.discountPrice= cartProduct.discountPrice;
        orderProduct.quantity= cartProduct.quantity;
        orderProduct.productId= cartProduct.productId;
        orderProduct.formattedPrice= cartProduct.formattedPrice;
        orderProduct.discountPriceFormatted= cartProduct.discountPriceFormatted;
        return orderProduct;
    }

    public static Model.Find<Long, OrderProduct> find = new Model.Find<Long, OrderProduct>() {
    };
}
