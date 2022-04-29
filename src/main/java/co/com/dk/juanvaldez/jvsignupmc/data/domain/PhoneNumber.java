package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumber implements Serializable {

    private static final long serialVersionUID = -3228787783470270554L;

    @NotNull
    @Min(2)
    @JsonProperty("code")
    private Integer code;

    @NotNull
    @Min(9)
    @JsonProperty("number")
    private Long number;

}
