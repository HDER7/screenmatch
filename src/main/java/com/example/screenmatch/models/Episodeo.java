package com.example.screenmatch.models;

import jakarta.persistence.*;

import java.time.DateTimeException;
import java.time.LocalDate;

@Entity
@Table(name = "episodeos")
public class Episodeo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodeo;
    private Double evaluacion;
    private LocalDate fechaLanzamiento;
    @ManyToOne()
    private Serie serie;

    public Episodeo() {}

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", numeroEpisodeo=" + numeroEpisodeo +
                ", evaluacion=" + evaluacion +
                ", fechaLanzamiento=" + fechaLanzamiento ;
    }

    public Episodeo(Integer numero, DatosEpisodeo d) {
        try{
            this.evaluacion = Double.valueOf(d.evaluacion());
        }catch(NumberFormatException e){
            this.evaluacion = 0.0;
        }
        try{
            this.fechaLanzamiento = LocalDate.parse(d.fechaDeLanzamiento());
        }catch(DateTimeException e){
            this.fechaLanzamiento = null;
        }
        this.numeroEpisodeo = d.numeroEpisodeo();
        this.titulo = d.titulo();
        this.temporada = numero;
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroEpisodeo() {
        return numeroEpisodeo;
    }

    public void setNumeroEpisodeo(Integer numeroEpisodeo) {
        this.numeroEpisodeo = numeroEpisodeo;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDate fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
}
