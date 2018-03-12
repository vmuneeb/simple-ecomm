package dto;


import play.data.validation.Constraints;

/**
 * Created by muneeb on 03/02/17.
 */
public class UserDto {

    @Constraints.Required
    @Constraints.MinLength(3)
    @Constraints.MaxLength(15)
    public String name;
    public String phone;

    public String building;
    public String street;
    public String area;
    public String city;
}
