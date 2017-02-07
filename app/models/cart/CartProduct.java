package models.cart;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by muneeb on 22/01/17.
 */

@Entity
@Table(
        uniqueConstraints=
        @UniqueConstraint(columnNames={"cart_id", "product_id"})
)
public class CartProduct extends Model{

    @Id
    public long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    public Cart cart;

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

    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();

    public static Model.Find<Long, CartProduct> find = new Model.Find<Long, CartProduct>() {
    };
}
