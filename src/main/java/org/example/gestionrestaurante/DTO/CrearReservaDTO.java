package org.example.gestionrestaurante.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrearReservaDTO {
    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy.")
    @NotNull(message = "La fecha de la reserva no puede estar en blanco.")
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate fecha;
    @NotNull(message = "La hora no puede estar en blanco.")
    private Long hora;
    @NotNull(message = "El número de personas no puede estar en blanco.")
    private Long numPersonas;
    private Long mesa;
    private Long cliente;
}
