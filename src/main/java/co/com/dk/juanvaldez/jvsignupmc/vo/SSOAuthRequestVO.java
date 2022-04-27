package co.com.dk.juanvaldez.jvsignupmc.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SSOAuthRequestVO {

    @JsonProperty("issuer")
    private String issuer;

    @JsonProperty("auth_key")
    private String authKey;

    @JsonProperty("auth_secret")
    private String authSecret;

}

