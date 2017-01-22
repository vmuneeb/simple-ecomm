package models.product;




import com.avaje.ebean.Model;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Product extends Model{

    @Id
    public long id;
    public String name;
    public double price;

    @JsonProperty("discount_price")
    public double discountPrice;

    @ManyToOne
    @JoinColumn(name = "category_id")
    public Category category;

    public String description;

    @JsonProperty("main_image")
    public String mainImage;

    public static Find<Long, Product> find = new Find<Long, Product>() {
    };
}

