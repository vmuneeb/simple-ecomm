package models.cart;



import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import models.User;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Cart extends Model{

    @Id
    public long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    public User userId;

    public int productCount;

    public long totalPrice;

    public String totalPriceFormatted;
    public String currency;

    public List<CartProduct> items;

    @CreatedTimestamp
    public Date createdTime;

    @UpdatedTimestamp
    public Date updatedTime;

    public static Model.Find<Long, Cart> find = new Model.Find<Long, Cart>() {
    };
}
