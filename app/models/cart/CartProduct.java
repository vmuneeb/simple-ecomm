package models.cart;

import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.product.Product;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by muneeb on 22/01/17.
 */

@Entity
public class CartProduct {

    @Id
    public long id;

    public long productId;

    public double price;

    public double discountPrice;

    @JsonProperty("main_image")
    public String mainImage;

    public static Model.Find<Long, CartProduct> find = new Model.Find<Long, CartProduct>() {
    };
}
