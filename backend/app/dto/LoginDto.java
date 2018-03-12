package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 09/01/17.
 */
public class LoginDto {

    @Constraints.Required
    public String email;
    @Constraints.Required
    public String password;

}
