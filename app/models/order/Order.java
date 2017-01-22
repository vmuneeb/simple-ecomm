package models.order;




public class Order {

    private long id;

    private String remoteId;

    private String dateCreated;
    private String status;
    private int total;

    private String totalFormatted;

    private String shippingName;

    private int shippingPrice;

    private String shippingPriceFormatted;
    private String currency;

    private long shippingType;

    private long paymentType;
    private String name;
    private String street;

    private String houseNumber;
    private String city;
    private String zip;

    private String email;
    private String phone;
    private String note;

}
