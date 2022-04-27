package co.com.dk.juanvaldez.jvsignupmc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SSOResponseVO<T> implements Serializable {

    private static final long serialVersionUID = -7743696715147629698L;

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "code")
    private int code;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "result")
    private T result;
}
