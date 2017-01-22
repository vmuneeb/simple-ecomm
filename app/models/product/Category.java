package models.product;

import com.avaje.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by muneeb on 21/01/17.
 */

@Entity
public class Category extends Model {

    @Id
    public long categoryId;

    public String name;

    public String image;

    public static Find<Long, Category> find = new Find<Long, Category>() {
    };
}
