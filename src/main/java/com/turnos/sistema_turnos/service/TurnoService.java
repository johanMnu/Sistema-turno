package com.turnos.sistema_turnos.service;

import com.turnos.sistema_turnos.exception.*;
import com.turnos.sistema_turnos.model.Turno;
import com.turnos.sistema_turnos.repository.TurnoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class TurnoService {
    private TurnoRepository turnoRepository;

    public TurnoService(TurnoRepository turnoRepository){
        this.turnoRepository = turnoRepository;
    }
    public Turno crearTurno(Turno turno){
        if (turno.getDuracionMinutos() == null || turno.getDuracionMinutos() <= 0) {
            throw new DuracionInvalidaException("La duracion del turno debe ser mayor a 0");
        }
        boolean existe=turnoRepository.
                findByFechaAndHora(turno.getFecha(),turno.getHora()).
                isPresent();
        if(existe){
            throw new TurnoDuplicadoException(
                    "Ya existe un turno para la fecha y hora seleccionadas"
            );
        }
        if(haySolapamiento(turno)){
            throw new HorarioNoDisponibleException(
                    "Ya existe un turno con ese horario"
            );
        }
        if (turno.getFecha().isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("El día del turno ya pasó");
        }
        turno.setEstado("RESERVADO");
        turno.setId(null);
        try {
            return turnoRepository.save(turno);
        }catch (DataIntegrityViolationException e){
            throw new HorarioNoDisponibleException(
                    "el horario ya esta ocupado"
            );
        }


    }
    public List<Turno>ObtenerTodos(){
      return   turnoRepository.findAll();
    }
    public void eliminar( Long id){
        if (!turnoRepository.existsById(id)){
            throw new TurnoNoEncontradoException(
                    "No existe un turno con id " + id
            );
        }
        turnoRepository.deleteById(id);
    }
    private LocalTime calcularHorFin(Turno turno){
        return turno.getHora().
                plusMinutes(turno.getDuracionMinutos());
    }
    public Turno obtenerTurnoPorId(Long id){
        return turnoRepository.findById(id).orElseThrow(() ->
                new TurnoNoEncontradoException(
                        "no existe un turno con id " + id
                )
        );

    }
    private boolean haySolapamiento(Turno nuevoTurno){
        List<Turno>turnosDelDia=
                turnoRepository.findByFecha(nuevoTurno.getFecha());
        LocalTime inicioNuevo = nuevoTurno.getHora();
        LocalTime finNuevo = calcularHorFin(nuevoTurno);
        for (Turno existente : turnosDelDia){
            LocalTime inicioExistente = existente.getHora();
            LocalTime finExistente = calcularHorFin(existente);

            boolean noSeSolapan =
                    finNuevo.isBefore(inicioExistente)
                    || finNuevo.equals(inicioExistente)
                    || inicioNuevo.isAfter(finExistente)
                    || inicioNuevo.equals(finExistente);
            if (!noSeSolapan) {
                return true;
            }
        }
        return false;
    }
}
