package dto;

import play.data.validation.Constraints;
import play.data.validation.ValidationError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by muneeb on 24/03/17.
 */
public class ResetPassWordDto {

    @Constraints.Email
    @Constraints.Required
    public  String email;

    @Constraints.Required
    public  String token;

    @Constraints.Required
    @Constraints.MinLength(3)
    public  String password1;


    @Constraints.Required
    @Constraints.MinLength(3)
    public  String password2;

    public List<ValidationError> validate() {
        List<ValidationError> errors = new ArrayList<>();
        if (!password1.equalsIgnoreCase(password2)) {
            errors.add(new ValidationError("password", "Password does'nt match"));
        }
        return errors.isEmpty() ? null : errors;
    }
}
