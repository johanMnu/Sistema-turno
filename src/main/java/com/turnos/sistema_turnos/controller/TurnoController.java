package com.turnos.sistema_turnos.controller;

import com.turnos.sistema_turnos.model.Turno;
import com.turnos.sistema_turnos.repository.TurnoRepository;
import com.turnos.sistema_turnos.service.TurnoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/turnos")
public class TurnoController {
    private TurnoService turnoService;

    public TurnoController(TurnoService turnoService){
        this.turnoService = turnoService;
    }
    @GetMapping
    public List<Turno>obtenerTurnos(){
        return turnoService.ObtenerTodos();
    }
    @PostMapping
    public Turno crearTurno(@RequestBody Turno turno){
        System.out.println(turno.getDuracionMinutos());
        return turnoService.crearTurno(turno);
    }
    @DeleteMapping("/{id}")
    public void eliminarTurno(@PathVariable Long id){
        turnoService.eliminar(id);
    }
}
