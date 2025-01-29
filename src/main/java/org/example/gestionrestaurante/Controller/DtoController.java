package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.DTO.ReservaDTO;
import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Repository.ClienteRepository;
import org.example.gestionrestaurante.Repository.MesaRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DtoController {
    @Autowired
    ReservaRepository reservaRepository;

    @GetMapping("/reservasPorFecha/{fecha}")
    public ResponseEntity<List<ReservaDTO>> getReservasPorFecha(@PathVariable("fecha") String fecha) {
        // Convertimos la fecha que recibimos en la URL a tipo LocalDate
        LocalDate fechaReserva = LocalDate.parse(fecha);

        List<ReservaDTO> reservasDTO = new ArrayList<>();

        // Buscamos las reservas para esa fecha
        reservaRepository.findByFecha(fechaReserva).forEach(reserva -> {
            reservasDTO.add(
                    ReservaDTO.builder()
                            .nombreCliente(reserva.getCliente().getNombre())
                            .apellidoCliente(reserva.getCliente().getApellidos())
                            .telfCliente(reserva.getCliente().getTelefono())
                            .fechaReserva(reserva.getFecha())
                            .horaReserva(reserva.getHora())
                            .numPersonas(reserva.getNumPersonas())
                            .numeroMesa(reserva.getMesa().getNumeroMesa())
                            .descripcionMesa(reserva.getMesa().getDescripcion())

                            .build()
            );
        });

        // Devolvemos la lista de DTOs como respuesta
        return ResponseEntity.ok(reservasDTO);
    }
}
