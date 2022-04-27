package co.com.dk.juanvaldez.jvsignupmc.data.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Prestamo implements Serializable {

    private static final long serialVersionUID = 3835034474148992522L;

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "isbn")
    private String isbn;

    @JsonProperty(value = "identificacionUsuario")
    private String identificacionUsuario;

    @JsonProperty(value = "tipoUsuario")
    private Integer tipoUsuario;

    @JsonProperty(value = "fechaMaximaDevolucion")
    private String fechaDevolucion;

}
