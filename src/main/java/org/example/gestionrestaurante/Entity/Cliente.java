package org.example.gestionrestaurante.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="clientes")
@Entity
@Builder
//lo pongo porque si no se hacia bucle en la respuesta de las reservas
@JsonIgnoreProperties({"user", "roles"})
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "El nombre no puede estar en blanco")
    @Size(min = 3, message = "El campo debe tener al menos 3 caracteres")
    private String nombre;
    @NotBlank(message = "Los apellidos no pueden estar en blanco")
    private String apellidos;
    @NotNull
    private Long telefono;
    @Column(unique = true, nullable = false)
    @NotNull(message = "El email no puede estar en blanco.")
    @Email(message = "Debe proporcionar un email válido")
    private String email;
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Reserva> reservas;
    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;
    private String username;



}
