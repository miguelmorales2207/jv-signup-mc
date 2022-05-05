package co.com.dk.juanvaldez.jvsignupmc.vo.request;

import co.com.dk.juanvaldez.jvsignupmc.vo.request.requestUser.RequestUserBody;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVO extends RequestUserBody implements Serializable {

    private static final long serialVersionUID = 2690685597252211031L;

    @NotNull
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    @JsonProperty("anonymous")
    private boolean anonymous = false;

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
    private PhoneNumberVO phoneNumberVO;

    @JsonProperty("birthdate")
    private Integer birthdate;

    @JsonProperty("cedula")
    private String cedula;

}
