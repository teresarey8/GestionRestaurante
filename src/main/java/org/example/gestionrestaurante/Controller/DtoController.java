package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.DTO.ReservaDTO;
import org.example.gestionrestaurante.Repository.ClienteRepository;
import org.example.gestionrestaurante.Repository.MesaRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DtoController {
    @Autowired
    ReservaRepository reservaRepository;
    MesaRepository mesaRepository;
    ClienteRepository clienteRepository;

    @GetMapping("/ListaTodo")
    public ResponseEntity<ReservaDTO> getTodo(){
        List<ReservaDTO> reservasDTO = new ArrayList<>();
        //for each
        reservaRepository.findAll().forEach(reserva -> {
            reservasDTO.add(
                    ReservaDTO.builder()
                            .fechaR(reserva.getFecha())
                            .horaR(reserva.getHora())
                            .numPersonasR(reserva.getNumPersonas())
                            .build()
            );
        });
        clienteRepository.findAll().forEach(cliente -> {
            reservasDTO.add(
                    ReservaDTO.builder()
                            .nombreC(cliente.getNombre())
                            .apellidosC(cliente.getApellidos())
                            .telefonoC(cliente.getTelefono())
                            .build()
            );
        });
        mesaRepository.findAll().forEach(mesa -> {
            reservasDTO.add(
                    ReservaDTO.builder()
                            .numeroMesa(mesa.getNumeroMesa())
                            .descripcionM(mesa.getDescripcion())
                            .build()
            );
        });
        return ResponseEntity.ok().build();
    }
}
