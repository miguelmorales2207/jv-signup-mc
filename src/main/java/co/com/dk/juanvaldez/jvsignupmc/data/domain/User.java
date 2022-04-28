package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.RequestUserBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User extends RequestUserBody {

    private static final long serialVersionUID = -3228787783470270554L;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("anonymous")
    private boolean anonymous;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("password")
    private String password;

    @JsonProperty("terms")
    private boolean terms;

    @JsonProperty("vendor")
    private Integer vendor;

    @JsonProperty("phone_number")
    private Object phoneNumber;

    @JsonProperty("birthdate")
    private Integer birthdate;

    @JsonProperty("cedula")
    private String cedula;

}
