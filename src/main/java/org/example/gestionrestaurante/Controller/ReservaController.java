package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Repository.ClienteRepository;
import org.example.gestionrestaurante.Repository.MesaRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MesaRepository mesaRepository;

    /**
     * Obtengo todas las reservas formato json
     */
    @GetMapping("/reservas")
    public ResponseEntity <List<Reserva>> getListReservas(){
        var reservas = reservaRepository.findAll();
        return ResponseEntity.ok(reservas);
    }
    /**
     * insertamos una reserva nueva
     */
    @PostMapping("/reservas")//y lo cogemos del body los datos necesarios
    public ResponseEntity<Reserva> insertReserva(@RequestBody Reserva reserva){
        var reservas = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservas);
    }
    /**
     * obtenemos una reserva especifica
     */
    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> getReserva(@PathVariable Long id){
        return reservaRepository.findById(id)
                //Si la reserva fue encontrada (Optional no vacío), se ejecuta el bloque dentro de map.
                //ResponseEntity.ok() crea una respuesta HTTP con el código de estado 200 OK.
                //body(resreva) añade el proyecto como cuerpo de la respuesta.
                .map(reserva -> ResponseEntity.ok().body(reserva))
                .orElse(ResponseEntity.notFound().build());
    }
    /**
     * modificamos una reserva
     */
    @PutMapping("/reservas/{id}")
    public ResponseEntity <Reserva> editReserva(@PathVariable Long id, @RequestBody Reserva nuevaReserva){
        return  reservaRepository.findById(id)
                .map(reserva -> {
                    reserva.setNumPersonas(nuevaReserva.getNumPersonas());
                    reserva.setHora(nuevaReserva.getHora());
                    reserva.setFecha(nuevaReserva.getFecha());
                    return ResponseEntity.ok().body(reservaRepository.save(reserva));
                })
                .orElseGet(() ->{
                    return ResponseEntity.notFound().build();
                });
    }
    /**
     * Eliminamos reserva
     */
    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Long id){
        Optional<Reserva> reserva = reservaRepository.findById(id);
        if(reserva.isPresent()){
            reservaRepository.delete(reserva.get());
            return ResponseEntity.noContent().build();
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    /**
     * AHORA LAS RELACIONES, QUE TENEMOS DOS, MESA Y CLIENTE.
     */











}
