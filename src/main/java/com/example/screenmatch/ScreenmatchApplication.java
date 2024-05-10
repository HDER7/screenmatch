package com.example.screenmatch;

import com.example.screenmatch.models.DatosEpisodeo;
import com.example.screenmatch.models.DatosSeries;
import com.example.screenmatch.models.DatosTemporada;
import com.example.screenmatch.principal.EjemploStreams;
import com.example.screenmatch.principal.Principal;
import com.example.screenmatch.repository.SerieRepository;
import com.example.screenmatch.service.ConsumoAPI;
import com.example.screenmatch.service.ConvertirDatos;
import com.example.screenmatch.service.IConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	@Autowired
	private SerieRepository repository;
	public static void main(String[] args) {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.muestraElMenu();
//		EjemploStreams ejemploStreams = new EjemploStreams();
//		ejemploStreams.muestraEjemplo();
	}
}
