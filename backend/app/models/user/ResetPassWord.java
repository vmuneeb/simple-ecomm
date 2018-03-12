package models.user;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by muneeb on 24/03/17.
 */
@Entity
public class ResetPassWord extends Model{
    @Id
    public long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    @Column(unique=true)
    public User user;

    public String token;

    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss")
    public Date expiry = new Date();

    public static Find<Long, ResetPassWord> find = new Model.Find<Long, ResetPassWord>() {
    };
}
