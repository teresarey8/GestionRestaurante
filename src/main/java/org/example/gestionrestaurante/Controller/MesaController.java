package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.Entity.Mesa;
import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Repository.MesaRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class MesaController {

    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Obtenemos todas las mesas de nuestro restaurante en json con paginacion
     */
    @GetMapping("/mesas")
    public ResponseEntity<List<Mesa>> getAllMesas() {
        var mesas = mesaRepository.findAll();
        return ResponseEntity.ok(mesas);
    }

    /**
     * Insertamos una mesa
     */
    @PostMapping("/mesas")
    public ResponseEntity<Mesa> addMesa(@RequestBody Mesa mesa) {
        var mesas = mesaRepository.save(mesa);
        return ResponseEntity.status(HttpStatus.CREATED).body(mesas);
    }

    /**
     * Obtenemos los datos de una mesa en concreto
     */
    @GetMapping("/mesas/{id}")
    public ResponseEntity<Mesa> getMesa(@PathVariable Long id) {
        return mesaRepository.findById(id)
                .map(mesa -> ResponseEntity.ok().body(mesa))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Modificamos una mesa
     */
    @PutMapping("/mesas/{id}")
    public ResponseEntity<Mesa> editMesa(@PathVariable Long id, @RequestBody Mesa nuevaMesa) {
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesa.setNumeroMesa(nuevaMesa.getNumeroMesa());
                    mesa.setDescripcion(nuevaMesa.getDescripcion());
                    return ResponseEntity.ok(mesaRepository.save(mesa));
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * eliminamos una mesita
     */
    @DeleteMapping("/mesas/{id}")
    public ResponseEntity<Mesa> deleteMesa(@PathVariable Long id) {
        Optional<Mesa> mesa = mesaRepository.findById(id);
        if (mesa.isPresent()) {
            mesaRepository.delete(mesa.get());
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //ahora añadimos las mesas a las reservas que correspondan
    @PostMapping("/mesas/{id}/reservas")
    public ResponseEntity<Mesa> insertReservas(@PathVariable Long id, @RequestBody Reserva reserva) {
        Optional<Mesa> mesa = mesaRepository.findById(id);
        Optional<Reserva> reservaBD = reservaRepository.findById(reserva.getId());
        if (mesa.isPresent() && reservaBD.isPresent()) {
            mesa.get().getReservas().add(reservaBD.get());
            mesaRepository.save(mesa.get());
            return ResponseEntity.ok(mesa.get());
        }
        return ResponseEntity.notFound().build();
    }

    //ahora podré borrar la mesa x de la reserva x
    @DeleteMapping("/mesas/{idMesa}/reservas/{idReserva}")
    public ResponseEntity<Mesa> deleteReservas(@PathVariable Long idMesa, @PathVariable Long idReserva) {
        Optional<Mesa> mesa = mesaRepository.findById(idMesa);
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (mesa.isPresent() && reserva.isPresent()) {
            mesa.get().getReservas().remove(reserva.get());
            return ResponseEntity.ok(mesa.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Ahora obtendremos las reservas de cada mesa
    @GetMapping("/mesas/{id}/reservas")
    public ResponseEntity<List<Reserva>> getAllReservas(@PathVariable Long id) {
        Optional<Mesa> mesa = mesaRepository.findById(id);
        if (mesa.isPresent() && mesa.get().getReservas().size() > 0) {
            return ResponseEntity.ok(mesa.get().getReservas());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}








