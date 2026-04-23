public class Address {

    private String name;
    private String street;
    private String city;
    private String pincode;

    public Address(String name, String street, String city, String pincode) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.pincode = pincode;
    }

    public void displayAddress() {
        System.out.println("Shipping Address:");
        System.out.println(name);
        System.out.println(street);
        System.out.println(city + " - " + pincode);
    }
}