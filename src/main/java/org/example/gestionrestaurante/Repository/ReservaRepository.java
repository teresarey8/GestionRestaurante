package org.example.gestionrestaurante.Repository;

import org.example.gestionrestaurante.Entity.Reserva;
import org.example.gestionrestaurante.Entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    List<Reserva> findByFecha(LocalDate fecha);
    List<Reserva> findByCliente_User(UserEntity user);
}
