package com.turnos.sistema_turnos.service;

import com.turnos.sistema_turnos.exception.DuracionInvalidaException;
import com.turnos.sistema_turnos.exception.FechaInvalidaException;
import com.turnos.sistema_turnos.exception.HorarioNoDisponibleException;
import com.turnos.sistema_turnos.exception.TurnoNoEncontradoException;
import com.turnos.sistema_turnos.model.Turno;
import com.turnos.sistema_turnos.repository.TurnoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TurnoServiceTest {
    @Mock
    private TurnoRepository turnoRepository;

    @InjectMocks
    private TurnoService turnoService;

    @Test
    void deberiaLanzarExcepcionSiDuracionEsMenorOIgualACero() {

        Turno turno = new Turno();
        turno.setDuracionMinutos(0);

        assertThrows(
                DuracionInvalidaException.class,
                () -> turnoService.crearTurno(turno)
        );
    }

    @Test
    void deberiaLanzarExcepcionSiHaySolapamiento() {

        Turno turnoExistente = new Turno();
        turnoExistente.setFecha(LocalDate.now());
        turnoExistente.setHora(LocalTime.of(10, 0));
        turnoExistente.setDuracionMinutos(60);

        Turno nuevoTurno = new Turno();
        nuevoTurno.setFecha(LocalDate.now());
        nuevoTurno.setHora(LocalTime.of(10, 30));
        nuevoTurno.setDuracionMinutos(30);

        when(turnoRepository.findByFecha(nuevoTurno.getFecha()))
                .thenReturn(List.of(turnoExistente));

        assertThrows(
                HorarioNoDisponibleException.class,
                () -> turnoService.crearTurno(nuevoTurno)
        );
    }
    @Test
    void deberiaLanzarExcepcionSiTurnoNoExiste() {

        when(turnoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                TurnoNoEncontradoException.class,
                () -> turnoService.obtenerTurnoPorId(1L)
        );
    }
    @Test
    void deberiaCrearTurnoCorrectamente() {

        Turno turno = new Turno();
        turno.setFecha(LocalDate.now());
        turno.setHora(LocalTime.of(9, 0));
        turno.setDuracionMinutos(30);

        when(turnoRepository.findByFecha(turno.getFecha()))
                .thenReturn(List.of());

        when(turnoRepository.save(turno))
                .thenReturn(turno);

        Turno resultado = turnoService.crearTurno(turno);

        assertNotNull(resultado);
        verify(turnoRepository).save(turno);
    }
    @Test
    void deberiaLanzarExcepcionSiFechaDelTurnoEsPasada() {

        Turno turno = new Turno();
        turno.setFecha(LocalDate.now().minusDays(1));
        turno.setHora(LocalTime.of(10, 0));
        turno.setDuracionMinutos(30);

        assertThrows(FechaInvalidaException.class, () -> {
            turnoService.crearTurno(turno);
        });
    }


}
