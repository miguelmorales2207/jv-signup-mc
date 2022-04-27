package co.com.dk.juanvaldez.jvsignupmc.security;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSecurity {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String phone;

    private Boolean allowAdmin;

    private Long defaultCompanyId;

    private Long countryId;

    private Long legacyId;

    private List<Company> companies;

    private Set<SimpleGrantedAuthority> roles;

}
