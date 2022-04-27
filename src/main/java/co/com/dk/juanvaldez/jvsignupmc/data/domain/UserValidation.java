package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserValidation implements Serializable {

    private static final long serialVersionUID = -3228787783470270554L;

    @JsonProperty("exists")
    private Boolean exists;

    @JsonProperty("valid")
    private Boolean valid;

    @JsonProperty("vendor")
    private Object vendor;

}
