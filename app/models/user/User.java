package models.user;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.format.Formats;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


@Entity
//@Table(name = "future_shop_user")
public class User extends Model{

    @Id
    public long id;


    @JsonProperty("access_token")
    public String accessToken;
    public String name;
    @JsonIgnore
    public String password;
    @Column(unique=true)
    public String email;
    public String phone;
    public String gender;

    public String building;
    public String street;
    public String area;
    public String city;

    public UserType userType = UserType.USER;

    @CreatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date createdTime = new Date();

    @UpdatedTimestamp
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date updatedTime = new Date();

    public static User authenticate(String email,String password) {
        User user = User.find.where().eq("email",email).findUnique();
        return user;
    }


    DateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:SS:mm");

    public String getUpdatedTime() {
        return format.format(updatedTime);
    }

    public static Find<Long, User> find = new Model.Find<Long, User>() {
    };

}
