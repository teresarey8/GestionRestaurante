package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.Config.JwtTokenProvider;
import org.example.gestionrestaurante.Entity.Mesa;
import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Entity.UserEntity;
import org.example.gestionrestaurante.Repository.ClienteRepository;
import org.example.gestionrestaurante.Repository.MesaRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.example.gestionrestaurante.Repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
public class ReservaController {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private UserEntityRepository userEntityRepository;
    /**
     * Obtengo todas las reservas formato json
     */
    @GetMapping("/reservas")
    public ResponseEntity<List<Reserva>> obtenerMisReservas(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String username = authentication.getName(); // Obtiene el username desde el token

        // Buscar el usuario en la base de datos
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Buscar las reservas del cliente asociado al usuario
        List<Reserva> reservas = reservaRepository.findByCliente_User(user);

        return ResponseEntity.ok(reservas);
    }



    /**
     * insertamos una reserva nueva, viendo la disponiblidad de mesas
     */
        @PostMapping("/reservas")
        public ResponseEntity<?> reservarMesa(@RequestBody Reserva CrearReservaDTO) {
            //por si no añaden bien la mesa o si no existe
            if (CrearReservaDTO.getMesa() == null || CrearReservaDTO.getMesa().getId() == null) {
                return ResponseEntity.badRequest().body("Debe seleccionar una mesa válida.");
            }
            //Llamamos al metodo del repositorio para comprobar si la mesa ya está reservada en esa fecha y hora y le enviamos los datos para que lo compruebe
            boolean ocupada = mesaRepository.existsReservaByMesaAndFechaAndHora(
                    CrearReservaDTO.getMesa().getId(),
                    CrearReservaDTO.getFecha(),
                    CrearReservaDTO.getHora()
            );

            if (ocupada) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La mesa ya está ocupada en ese horario.");
            }
            //aqui creamos una reserva de verdad, basandonos en el dto
            Reserva reserva = Reserva.builder()
                    .fecha(CrearReservaDTO.getFecha())
                    .mesa(CrearReservaDTO.getMesa())
                    .hora(CrearReservaDTO.getHora())
                    .cliente(CrearReservaDTO.getCliente())
                    .numPersonas(CrearReservaDTO.getNumPersonas())
                    .build();

            Reserva nuevaReserva = reservaRepository.save(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
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
