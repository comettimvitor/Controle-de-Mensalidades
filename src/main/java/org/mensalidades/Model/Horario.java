package org.mensalidades.Model;

import java.time.LocalTime;
import java.util.Date;

public class Horario {
    private Long id;
    private LocalTime horarioInicio;
    private LocalTime HorarioFim;
    private Date inicioVigencia;
    private Date fimVigencia;

    public Horario() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return HorarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        HorarioFim = horarioFim;
    }

    public Date getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(Date inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public Date getFimVigencia() {
        return fimVigencia;
    }

    public void setFimVigencia(Date fimVigencia) {
        this.fimVigencia = fimVigencia;
    }
}
