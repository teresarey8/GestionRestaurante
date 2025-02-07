package org.example.gestionrestaurante.Controller;

import jakarta.annotation.PostConstruct;
import org.example.gestionrestaurante.Config.JwtTokenProvider;
import org.example.gestionrestaurante.DTO.LoginRequestDTO;
import org.example.gestionrestaurante.DTO.LoginResponseDTO;
import org.example.gestionrestaurante.DTO.UserRegisterDTO;
import org.example.gestionrestaurante.Entity.Rol;
import org.example.gestionrestaurante.Entity.UserEntity;
import org.example.gestionrestaurante.Repository.RolRepository;
import org.example.gestionrestaurante.Repository.UserEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class AuthController {
    @Autowired
    private UserEntityRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    RolRepository rolRepository;

    // Esta anotación indica que este método se ejecutará después de que se haya inicializado el bean
    @PostConstruct
    public void init() {
        //Verifica si no hay roles en la base de datos, asi no se crean cada vez que la app se enciende
        if (rolRepository.count() == 0) {
            // Si no existen roles, los creamos
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre("ROLE_ADMIN");

            Rol rolUser = new Rol();
            rolUser.setNombre("ROLE_USER");

            // Guardamos los roles en la base de datos
            rolRepository.save(rolAdmin);
            rolRepository.save(rolUser);

            System.out.println("Roles creados: " + rolAdmin.getNombre() + ", " + rolUser.getNombre());
        }
    }

    //me viene un usuario y lo tengo que guardar en la base de datos
    //transformo el userdto a userentity, para cuando queremos neviar o recibir que no coinciden exactamente con los detalles de la entidad
    //cuando lo tenga guardado mandandamos unas respuesta
    //userRegisterDTO, and login
    //cuando se haco login devolvemos el nombre y el token
    //cuadno se registra lo convertimos a entity y lo creamos y guardamos pq hay cosas que no queremos que ponga o q vea q en la entidad si está.
    @PostMapping("/auth/register")
    public ResponseEntity<UserEntity> save(@RequestBody UserRegisterDTO userDTO) {
        // Validamos que el rol no sea nulo ni vacío
        if (userDTO.getRoles() == null || userDTO.getRoles().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // Buscamos el rol en la base de datos
        Rol rol = rolRepository.findByNombre(userDTO.getRoles())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + userDTO.getRoles()));

        // Crear usuario con el rol asignado
        UserEntity userEntity = UserEntity.builder()
                .username(userDTO.getUsername())
                .password(passwordEncoder.encode(userDTO.getPassword()))
                .email(userDTO.getEmail())
                .roles(Set.of(rol)) // Asignamos el único rol
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
    }



    //recibe un objeto;usuario y password
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginDTO) {
        try {

            //Validamos al usuario en Spring (hacemos login manualmente)
            UsernamePasswordAuthenticationToken userPassAuthToken = new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword());
            Authentication auth = authenticationManager.authenticate(userPassAuthToken);    //valida el usuario y devuelve un objeto Authentication con sus datos
            //Obtenemos el UserEntity del usuario logueado
            UserEntity user = (UserEntity) auth.getPrincipal();

            //Generamos un token con los datos del usuario (la clase tokenProvider ha hemos creado nosotros para no poner aquí todo el código
            String token = this.tokenProvider.generateToken(auth);

            //Devolvemos un código 200 con el username y token JWT
            return ResponseEntity.ok(new LoginResponseDTO(user.getUsername(), token));
        }catch (Exception e) {  //Si el usuario no es válido, salta una excepción BadCredentialsException
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    Map.of(
                            "path", "/auth/login",
                            "message", "Credenciales erróneas",
                            "timestamp", new Date()
                    )
            );
        }
    }
}