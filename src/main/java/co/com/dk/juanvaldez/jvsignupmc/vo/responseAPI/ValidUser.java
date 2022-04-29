package co.com.dk.juanvaldez.jvsignupmc.vo.responseAPI;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ValidUser implements Serializable {

    private static final long serialVersionUID = -3228787783470270554L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email_address")
    private String emailAddress;

    @JsonProperty("birthdate")
    private Date birthdate;

    @JsonProperty("date_created")
    private Date dateCreated;

    @JsonProperty("date_updated")
    private Date dateUpdated;

    @JsonProperty("status")
    private Status status;

}
