package models.cart;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
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

    @Inject
    @Transient
    private Config config = ConfigFactory.load();

    @Transient
    String BUCKET = "aws.s3.bucket";

    @Id
    public long id;

    @ManyToOne
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

    private String getActualFileName() {
        return "products" + "/" + mainImage;
    }

    public String getMainImage() {
        return config.getString("aws.url") + config.getString(BUCKET)+"/"+getActualFileName();
    }

    public static Model.Find<Long, CartProduct> find = new Model.Find<Long, CartProduct>() {
    };
}
