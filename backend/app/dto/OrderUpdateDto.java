package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 18/03/17.
 */
public class OrderUpdateDto {
    @Constraints.Required
    public String status;
}
