package co.com.dk.juanvaldez.jvsignupmc.vo.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserValidation implements Serializable {

    private static final long serialVersionUID = -3228787783470270554L;

    @JsonProperty("exists")
    private boolean exists = false;

    @JsonProperty("valid")
    private boolean valid;

    @JsonProperty("vendor")
    private Object vendor;

}
