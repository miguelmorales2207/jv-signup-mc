package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.RequestUserBody;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User extends RequestUserBody {

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "email_address")
    private String emailAddress;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;

    @JsonProperty(value = "identification_card")
    private String identificationCard;

    public User() {
    }

    public User(String firstName, String lastName, String emailAddress, String phoneNumber,
        String identificationCard) {
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.identificationCard = identificationCard;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getIdentificationCard() {
        return identificationCard;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setIdentificationCard(String identificationCard) {
        this.identificationCard = identificationCard;
    }

    //@Override
    /*public String toString() {
        return "CategoriaVO [id=" + id + ", nombre=" + nombre + "]";
    }*/
}
