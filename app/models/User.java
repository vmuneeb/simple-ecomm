package models;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
public class User extends Model{

    @Id
    private long id;


    public String accessToken;
    public String name;
    public String password;
    public String street;
    public String city;

    public String houseNumber;
    public String zip;
    public String email;
    public String phone;
    public String gender;
    public String country;

    public static Find<Long, User> find = new Find<Long, User>() {};
}
