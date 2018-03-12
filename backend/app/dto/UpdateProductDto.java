package dto;


import play.data.format.Formats;
import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muneeb on 21/02/17.
 */
public class UpdateProductDto {
    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(20)
    public String name;

    @Constraints.Required
    public double price;

    @Constraints.Required
    public double discountPrice;

    @Constraints.Required
    @Formats.NonEmpty
    public int category;

    @Constraints.Required
    @Formats.NonEmpty
    public int subCategory;

    @Constraints.Required
    @Formats.NonEmpty
    public int brand;

    @Constraints.Required
    public int quantity;

    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(100)
    public String desc;

    @Constraints.Required
    public boolean featured = false;

/*    @Constraints.Required
    public String image;*/

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (price < discountPrice) {
            errors.add(new ValidationError("price", "Discount price should be less than price"));
        }
        if(price < 0) {
            errors.add(new ValidationError("price", "Price should be valid"));
        }
        return errors.isEmpty() ? null : errors;
    }
}
