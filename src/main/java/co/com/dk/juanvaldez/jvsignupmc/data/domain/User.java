package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import co.com.dk.juanvaldez.jvsignupmc.data.domain.requestUser.RequestUserBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends RequestUserBody {

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
    private String phoneNumber;

    @JsonProperty("cedula")
    private String cedula;

}
