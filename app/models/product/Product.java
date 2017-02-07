package models.product;




import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.format.Formats;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Product extends Model{

    @Id
    public long id;
    public String name;
    public double price;

    @JsonProperty("discount_price")
    public double discountPrice;

    @JsonProperty("price_formatted")
    public String formattedPrice;

    @JsonProperty("discount_price_formatted")
    public String discountPriceFormatted;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    public String description;

    @JsonProperty("main_image")
    public String mainImage;

    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();

    public static Find<Long, Product> find = new Find<Long, Product>() {
    };
}

