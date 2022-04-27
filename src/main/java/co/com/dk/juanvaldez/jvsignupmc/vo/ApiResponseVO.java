package co.com.dk.juanvaldez.jvsignupmc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseVO<T> implements Serializable {

    private static final long serialVersionUID = -4997447450628067757L;

    @JsonProperty(value = "code")
    private int code;

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "error")
    private List<String> error;

    @JsonProperty(value = "data")
    private T data;

}
