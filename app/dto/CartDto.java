package dto;

/**
 * Created by muneeb on 10/01/17.
 */
public class CartDto {
    public int quantity;
    public CartProduct product;

}

class CartProduct {
    public String id;
    public String color_id;
    public String size_id;
}
