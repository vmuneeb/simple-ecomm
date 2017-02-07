package models;

import com.avaje.ebean.Model;
import com.avaje.ebean.annotation.CreatedTimestamp;
import com.avaje.ebean.annotation.UpdatedTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import play.data.format.Formats;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;


@Entity
public class User extends Model{

    @Id
    public long id;


    @JsonProperty("access_token")
    public String accessToken;
    public String name;
    @JsonIgnore
    public String password;
    public String street;
    public String city;

    public String houseNumber;
    public String zip;
    public String email;
    public String phone;
    public String gender;
    public String country;

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



    public static Find<Long, User> find = new Model.Find<Long, User>() {
    };

}
