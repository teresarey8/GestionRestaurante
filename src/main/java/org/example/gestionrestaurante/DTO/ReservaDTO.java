package org.example.gestionrestaurante.DTO;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservaDTO {
    //de mesa
    private Long numeroMesa;
    private String descripcionM;
    //de cliente
    private String nombreC;
    private String apellidosC;
    private Long telefonoC;
    //ahora de la reserva
    private LocalDate fechaR;
    private Long horaR;
    private Long numPersonasR;

}
