package org.example.gestionrestaurante.Repository;

import org.example.gestionrestaurante.Entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    //un boolean que sea si o no
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.mesa.id = :mesaId AND r.fecha = :fecha AND r.hora = :hora")
    boolean existsReservaByMesaAndFechaAndHora(@Param("mesaId") Long mesaId, @Param("fecha") LocalDate fecha, @Param("hora") Long hora);
    @Query("SELECT m FROM Mesa m WHERE m.id NOT IN " +
            "(SELECT r.mesa.id FROM Reserva r WHERE r.fecha = :fecha AND r.hora = :hora)")
    List<Mesa> findMesasDisponibles(@Param("fecha") LocalDate fecha, @Param("hora") Long hora);

}
