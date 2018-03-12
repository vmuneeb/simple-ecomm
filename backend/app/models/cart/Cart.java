package models.cart;



import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.PrivateOwned;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.user.User;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Cart extends Model{

    @Id
    public long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;

    @JsonProperty("product_count")
    public int productCount;

    @JsonProperty("price_formatted")
    public long totalPrice;

    @JsonProperty("total_price_formatted")
    public String totalPriceFormatted;
    public String currency;

    @PrivateOwned
    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL)
    @JsonManagedReference
    public List<CartProduct> items;

    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();

    public static Model.Find<Long, Cart> find = new Model.Find<Long, Cart>() {
    };
}
