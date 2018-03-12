package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 14/03/17.
 */
public class SubCategoryDto {
    @Constraints.Required
    public String name;

    @Constraints.Required
    public String image;
}
