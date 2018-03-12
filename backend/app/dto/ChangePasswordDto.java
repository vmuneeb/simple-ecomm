package dto;

import play.data.validation.Constraints;

/**
 * Created by muneeb on 03/02/17.
 */
public class ChangePasswordDto {
    @Constraints.Required
    public String old_password;

    public String getOld_password() {
        return old_password;
    }

    public void setOld_password(String old_password) {
        this.old_password = old_password;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    @Constraints.Required
    public String new_password;
}
