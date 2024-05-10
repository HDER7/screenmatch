package com.example.screenmatch.models;

import com.example.screenmatch.service.ConsultaChatGPT;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;

import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true, nullable = false)
    private String titulo;
    private Integer totalDeTemporadas;
    private Double evaluacion;
    private String poster;
    @Enumerated(EnumType.STRING)
    private Categoria genero;
    private String actores;
    private String sinopsis;
    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodeo> episodeos;

    public Serie(DatosSeries datosSeries) {
        this.titulo = datosSeries.titulo();
        this.totalDeTemporadas = datosSeries.totalDeTemporadas();
        this.evaluacion = OptionalDouble.of(Double.parseDouble(datosSeries.evaluacion())).orElse(0);
        this.poster = datosSeries.poster();
        this.genero = Categoria.fromString(datosSeries.genero().split(",")[0].trim());
        this.actores = datosSeries.actores();
        //this.sinopsis = ConsultaChatGPT.obtenerTraduccion(datosSeries.sinopsis());
        this.sinopsis = datosSeries.sinopsis();
    }

    public Serie() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalDeTemporadas() {
        return totalDeTemporadas;
    }

    public void setTotalDeTemporadas(Integer totalDeTemporadas) {
        this.totalDeTemporadas = totalDeTemporadas;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public List<Episodeo> getEpisodeos() {
        return episodeos;
    }

    public void setEpisodeos(List<Episodeo> episodeos) {
        episodeos.forEach(e -> e.setSerie(this));
        this.episodeos = episodeos;
    }

    @Override
    public String toString() {
        return "titulo='" + titulo + '\'' +
                ", totalDeTemporadas=" + totalDeTemporadas +
                ", evaluacion=" + evaluacion +
                ", poster='" + poster + '\'' +
                ", genero=" + genero +
                ", actores='" + actores + '\'' +
                ", sinopsis='" + sinopsis + '\'' +
                ", episodeos=" + episodeos;
    }
}
