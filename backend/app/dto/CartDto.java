package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 10/01/17.
 */
public class CartDto {
    @Constraints.Required
    public int quantity;
    @Constraints.Required
    public long product_variant_id;

}
