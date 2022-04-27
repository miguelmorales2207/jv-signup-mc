package co.com.dk.juanvaldez.jvsignupmc.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Company {

    private Long id;

    private String name;

    private String timezone;

}
