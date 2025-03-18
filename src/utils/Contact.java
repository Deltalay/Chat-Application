package utils;

import java.io.Serializable;

public class Contact implements Serializable {
    private String contact;
    private String sender;
    private String message;
    public Contact(String contact,String message) {
        this.contact = contact;
        this.message = message;
    }
    public String getContact() {
        return contact;
    }
    public String getMessage() {
        return message;
    }
}
