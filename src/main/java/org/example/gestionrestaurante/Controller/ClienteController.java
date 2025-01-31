package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.Entity.Cliente;
import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Repository.ClienteRepository;
import org.example.gestionrestaurante.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClienteController {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Obtengo todos los clientes en formato Json
     */
    @GetMapping("/clientes")
    public ResponseEntity <List<Cliente>> getListClientes() {
        var clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }
    /**
     * Insertamos un cliente con los datos que recibe del body en formato json
     */
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> insertCliente(@RequestBody Cliente cliente) {
        var clientes = clienteRepository.save(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(clientes);
    }
    /**
     * Obtenemos un cliente en concreto por su id
     */
     @GetMapping("/clientes/{id}")
    //La anotación @PathVariable vincula el valor {id} de la URL al parámetro id del método.
    public ResponseEntity<Cliente> getCliente(@PathVariable Long id) {
         //no devuelve un optional, devuelve el cliente o si no nos devuelve nulo
         //El métoodo findById devuelve un Optional
         return clienteRepository.findById(id)
                 //Si el reserva fue encontrado (Optional no vacío), se ejecuta el bloque dentro de map.
                 //ResponseEntity.ok() crea una respuesta HTTP con el código de estado 200 OK.
                 //body(reserva) añade el reserva como cuerpo de la respuesta.
                 .map(cliente -> ResponseEntity.ok().body(cliente))
                 .orElse(ResponseEntity.notFound().build());//404 not found
    }
    /**
     * Modificamos un cliente
     */
    @PutMapping("/clientes/{id}")
    //los nuevos datos del cliente vienen en ele body y se crea un objeto
    public ResponseEntity<Cliente> editCliente(@RequestBody Cliente NuevoCliente, @PathVariable Long id) {
        //optional, los buscamos en la bd y lo actualizamos, si no lo guardamos directamente
        return clienteRepository.findById(id)
        //se llama a ese cliente y le vamos asignando los datos que vienen en el body que serán los datos corregidos
                .map(cliente -> {
                    cliente.setNombre(NuevoCliente.getNombre());
                    cliente.setApellidos(NuevoCliente.getApellidos());
                    cliente.setTelefono(NuevoCliente.getTelefono());
                    cliente.setEmail(NuevoCliente.getEmail());
                    return ResponseEntity.ok(clienteRepository.save(cliente));
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();// 404 not found
                });
    }
    /**
     * Eliminamos clientes
     */
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        //uso optional por que si el Cliente existe:
        //El Optional contiene el valor del Cliente, y puedes acceder a él usando cliente.get().
        //El Cliente no existe:
        //El Optional estará vacío, lo cual puedes verificar con el método cliente.isPresent() o cliente.isEmpty().
        Optional<Cliente> cliente = clienteRepository.findById(id);
        if (cliente.isPresent()) {
            clienteRepository.delete(cliente.get());
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.notFound().build();
        }
    }
    //ahora tengo qur añadir las reservas del cliente, RELACION
    @PostMapping("/clientes/{id}/reservas")
    public ResponseEntity<Cliente> insertReserva(@PathVariable Long idCliente, @RequestBody Reserva reserva) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        Optional<Reserva> reservaBD = reservaRepository.findById(reserva.getId());
        if (cliente.isPresent() && reservaBD.isPresent()) {
            cliente.get().getReservas().add(reservaBD.get());
            clienteRepository.save(cliente.get());
            return ResponseEntity.ok(cliente.get());
        }
        return ResponseEntity.notFound().build();
    }
    //ahora para eliminar por ejemplo la reserva 10 del cliente 5
    @DeleteMapping("/clientes/{idCliente}/reserva/{idReserva}")
    public ResponseEntity<Cliente> deleteReserva(@PathVariable Long idCliente, @PathVariable Long idReserva) {
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        Optional<Reserva> reserva = reservaRepository.findById(idReserva);
        if (cliente.isPresent() && reserva.isPresent()) {
            cliente.get().getReservas().remove(reserva.get());
            return ResponseEntity.ok(cliente.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
        //ahora para obtener las reservas de un cliente en concreto
        @GetMapping("/clientes/{id}/reservas")
        //Y nos devuelve una lista de reservas
        public ResponseEntity<List<Reserva>> getReserva(@PathVariable Long id){
            Optional<Cliente> cliente = clienteRepository.findById(id);
            if (cliente.isPresent() && cliente.get().getReservas().size() > 0) {
                return ResponseEntity.ok(cliente.get().getReservas());
            }else{
                return ResponseEntity.notFound().build();
            }
        }


}
