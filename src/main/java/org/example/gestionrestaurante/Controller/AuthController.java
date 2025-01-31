package org.example.gestionrestaurante.Controller;

import org.example.gestionrestaurante.Config.JwtTokenProvider;
import org.example.gestionrestaurante.DTO.LoginRequestDTO;
import org.example.gestionrestaurante.DTO.LoginResponseDTO;
import org.example.gestionrestaurante.DTO.UserRegisterDTO;
import org.example.gestionrestaurante.Entity.UserEntity;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

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

    //me viene un usuario y lo tengo que guardar en la base de datos
    //transformo el userdto a userentity, para cuando queremos neviar o recibir que no coinciden exactamente con los detalles de la entidad
    //cuando lo tenga guardado mandandamos unas respuesta
    //userRegisterDTO, and login
    //cuando se haco login devolvemos el nombre y el token
    //cuadno se registra lo convertimos a entity y lo creamos y guardamos pq hay cosas que no queremos que ponga o q vea q en la entidad si está.
    @PostMapping("/auth/register")
    public ResponseEntity<UserEntity> save(@RequestBody UserRegisterDTO userDTO) {
        UserEntity userEntity = this.userRepository.save(
                UserEntity.builder()
                        .username(userDTO.getUsername())
                        .password(passwordEncoder.encode(userDTO.getPassword()))
                        .email(userDTO.getEmail())
                        .authorities(List.of("ROLE_USER", "ROLE_ADMIN"))
                        .build());
        //y te devuelve todo lo que hemos creado, se puede cambiar y poner arriba ? y que devuelva lo que quiera.
        return ResponseEntity.status(HttpStatus.CREATED).body(userEntity);
        //try catch para capturar excepciones

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