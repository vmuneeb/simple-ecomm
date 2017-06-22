package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 24/03/17.
 */
public class ResetDto {

    @Constraints.Email
    @Constraints.Required
    public  String email;

    @Constraints.Required
    public  String token;
}
