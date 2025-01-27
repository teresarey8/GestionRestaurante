package org.example.gestionrestaurante.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="reservas")
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @FutureOrPresent(message = "La fecha no puede ser anterior al día de hoy.")
    @NotBlank(message = "La fecha de la reserva no puede estar en blanco.")
    private LocalDate fecha;
    @NotBlank(message = "La hora no puede estar en blanco.")
    private Long hora;
    @NotBlank(message = "El número de personas no puede estar en blanco.")
    private Long numPersonas;
    //cliente y mesa
    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

}
