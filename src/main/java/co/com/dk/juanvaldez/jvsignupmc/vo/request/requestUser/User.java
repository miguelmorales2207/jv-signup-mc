package co.com.dk.juanvaldez.jvsignupmc.vo.request.requestUser;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 5336596442001280152L;

    @JsonProperty("id")
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("allow_admin")
    private Boolean allowAdmin;

    @JsonProperty("default_company_id")
    private Long defaultCompanyId;

    @JsonProperty("country_id")
    private Long countryId;

    @JsonProperty("legacy_id")
    private Long legacyId;

    @JsonProperty("companies")
    private List<Company> companies;

}
