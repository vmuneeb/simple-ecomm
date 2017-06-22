package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 21/02/17.
 */
public class BrandDto {
    @Constraints.Required
    public String name;
    @Constraints.Required
    public String image;
}
