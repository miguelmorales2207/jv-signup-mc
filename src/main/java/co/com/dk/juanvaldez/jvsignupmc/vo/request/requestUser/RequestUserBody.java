package co.com.dk.juanvaldez.jvsignupmc.vo.request.requestUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserBody implements Serializable {

    private static final long serialVersionUID = -3228787783470270554L;

    @JsonProperty("user")
    private User user;

}
