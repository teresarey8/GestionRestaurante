package org.example.gestionrestaurante.Repository;

import org.example.gestionrestaurante.Entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    // Método para obtener mesas disponibles
    @Query("SELECT m FROM Mesa m WHERE m.id = :mesaId AND NOT EXISTS (" +
            "SELECT r FROM Reserva r WHERE r.mesa = m AND r.fecha = :fecha AND r.hora = :hora)")
    List<Mesa> findMesasDisponibles(@Param("fecha") LocalDate fecha, @Param("hora") Long hora, @Param("mesaId") Long mesaId);


}
