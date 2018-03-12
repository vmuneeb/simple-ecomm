package dto;

import play.data.validation.Constraints;

import javax.validation.Constraint;


/**
 * Created by muneeb on 02/02/17.
 */
public class OrderDto {

    @Constraints.Required
    public String name;

    @Constraints.Required
    public String building;

    @Constraints.Required
    public String street;

    @Constraints.Required
    public String area;

    @Constraints.Required
    public String city;

    @Constraints.Required
    @Constraints.Email
    public String email;

    @Constraints.Required
    public String phone;

    public String note;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }



    @Override
    public String toString() {
        return "OrderDto{" +
                "name='" + name + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}
