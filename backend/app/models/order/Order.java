package models.order;



import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import models.user.User;
import play.data.format.Formats;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user_order")
public class Order extends Model{

    @Id
    public long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;

    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonManagedReference
    public List<OrderProduct> items;


    public OrderStatus status;

    public int total;

    @JsonProperty("total_formatted")
    public String totalFormatted;

    public String name;

    public String building;
    public String street;
    public String area;
    public String city;

    public String email;
    public String phone;
    public String note;


    public int productCount;

    public double totalPrice;


    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonProperty("date_created")
    public Date updatedTime = new Date();



    public static Model.Find<Long, Order> find = new Model.Find<Long, Order>() {
    };

    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:SS:mm");

    public String getUpdatedTime() {
        return format.format(updatedTime);
    }

    public String getCreatedTime() {
        return format.format(createdTime);
    }
}
