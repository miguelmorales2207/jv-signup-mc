package co.com.dk.juanvaldez.jvsignupmc.vo.request;

import co.com.dk.juanvaldez.jvsignupmc.vo.request.requestUser.RequestUserBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User extends RequestUserBody {

    private static final long serialVersionUID = -3228787783470270554L;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("anonymous")
    private boolean anonymous;

    @NotNull
    @JsonProperty("email_address")
    private String emailAddress;

    @NotNull
    @JsonProperty("password")
    private String password;

    @NotNull
    @JsonProperty("terms")
    private boolean terms;

    @NotNull
    @JsonProperty("vendor")
    private Integer vendor;

    @JsonProperty("phone_number")
    private PhoneNumber phoneNumber;

    @JsonProperty("birthdate")
    private Integer birthdate;

    @JsonProperty("cedula")
    private String cedula;

}
