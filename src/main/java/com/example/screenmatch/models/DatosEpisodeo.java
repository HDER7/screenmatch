package com.example.screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosEpisodeo(@JsonAlias("Title") String titulo,
                            @JsonAlias("Episode") Integer numeroEpisodeo,
                            @JsonAlias("imdbRating") String evaluacion,
                            @JsonAlias("Released") String fechaDeLanzamiento) {
}
