package org.example.gestionrestaurante.Repository;

import org.example.gestionrestaurante.Entity.Mesa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MesaRepository extends JpaRepository<Mesa, Long> {
    // Método para obtener mesas disponibles
    List<Mesa> findMesasDisponibles(LocalDate fecha, Long hora, Long numPersonas);
}
