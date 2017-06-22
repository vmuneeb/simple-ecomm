package models.order;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import models.cart.CartProduct;

import javax.persistence.*;

/**
 * Created by muneeb on 22/01/17.
 */

@Entity
public class OrderProduct extends Model{

    @Inject
    @Transient
    private Config config = ConfigFactory.load();


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

    @Transient
    String BUCKET = "aws.s3.bucket";


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

    private String getActualFileName() {
        return "products" + "/" + mainImage;
    }

    public String getMainImage() {
        return config.getString("aws.url") + config.getString(BUCKET)+"/"+getActualFileName();
    }

    public static Model.Find<Long, OrderProduct> find = new Model.Find<Long, OrderProduct>() {
    };
}
