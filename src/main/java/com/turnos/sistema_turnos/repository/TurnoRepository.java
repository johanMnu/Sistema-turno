package com.turnos.sistema_turnos.repository;

import com.turnos.sistema_turnos.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TurnoRepository extends JpaRepository<Turno,Long> {
    Optional<Turno>findByFechaAndHora(LocalDate fecha, LocalTime hora);
    List<Turno>findByFecha(LocalDate fecha);
}
