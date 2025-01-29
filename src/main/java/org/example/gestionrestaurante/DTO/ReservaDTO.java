package org.example.gestionrestaurante.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@Builder
public class ReservaDTO {
    //clientes
    private String nombreCliente;
    private String apellidoCliente;
    private Long telfCliente;
    //reserva
    private LocalDate fechaReserva;
    private Long horaReserva;
    //mesa
    private Long numeroMesa;
    private String descripcionMesa;
    private Long numPersonas;

}
