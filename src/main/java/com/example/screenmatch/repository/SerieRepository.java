package com.example.screenmatch.repository;

import com.example.screenmatch.models.Categoria;
import com.example.screenmatch.models.Episodeo;
import com.example.screenmatch.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String nombreSerie);
    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);
    //List<Serie>findByTotalDeTemporadasLessThanEqualAndEvaluacionGreaterThanEqual(int totalDeTemporadas, int evaluacion);

    @Query(value = "SELECT * FROM series s WHERE s.total_de_temporadas <= :temporadas AND s.evaluacion >= :evaluacion", nativeQuery = true)
    List<Serie> seriesPorTemparadaYEvaluacion(int temporadas, Double evaluacion);


    @Query(value = "SELECT e FROM Serie s JOIN s.episodeos e WHERE e.titulo ILIKE %:nombre%")
    List<Episodeo> episodeosPorNombre(String nombre);

    @Query(value = "SELECT e FROM Serie s JOIN s.episodeos e WHERE s =:serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodeo> top5Episodeos(Serie serie);

}
