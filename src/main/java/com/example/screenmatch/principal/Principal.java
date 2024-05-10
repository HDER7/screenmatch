package com.example.screenmatch.principal;

import com.example.screenmatch.models.*;
import com.example.screenmatch.repository.SerieRepository;
import com.example.screenmatch.service.ConsumoAPI;
import com.example.screenmatch.service.ConvertirDatos;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=3bf2bfdb";
    private final ConvertirDatos conversor = new ConvertirDatos();
    private final List<DatosSeries> datosSeries = new ArrayList<>();
    private final SerieRepository repository;
    private List<Serie> series;
    private Optional<Serie> serieOptional;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar serie por titulo
                    5 - Top 5 Series
                    6 - Buscar serie por categoria
                    7 - filtrar series por temporadas y evaluación
                    8 - Buscar episodios por titulo
                    9 - Top 5 Episodeos por serie
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    top5Series();
                    break;
                case 6:
                    buscarSeriaPorCategoria();
                    break;
                case 7:
                    filtrarSeriesPorTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodeosPorTitulo();
                    break;
                case 9:
                    top5EpisodeosSerie();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSeries getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSeries datos = conversor.obtenerDatos(json, DatosSeries.class);
        return datos;
    }
    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escoge la serie de la cual quieres ver los episodeos");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DatosTemporada datosTemporada = conversor.obtenerDatos(json, DatosTemporada.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodeo> episodeos = temporadas.stream()
                    .flatMap(d -> d.episodeos().stream()
                            .map(e -> new Episodeo(d.numero(),e)))
                    .toList();
            serieEncontrada.setEpisodeos(episodeos);
            repository.save(serieEncontrada);

        }
    }
    private void buscarSerieWeb() {
        DatosSeries datos = getDatosSerie();
        Serie serie = new Serie(datos);
        repository.save(serie);
        //datosSeries.add(datos);
        System.out.println(datos);
    }

    private void mostrarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        serieOptional = repository.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieOptional.isPresent()) {
            System.out.println("La serie buscada es: "+serieOptional.get().getTitulo());
        }
        else{
            System.out.println("No se encontro el serie");
        }
    }

    private void top5Series() {
        List<Serie> topSeries = repository.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " Evaluacion: " + s.getEvaluacion()));
    }

    private void buscarSeriaPorCategoria() {
        System.out.println("Escribe el genero/categoria de la serie a buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspain(genero);
        List<Serie> serieList = repository.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        serieList.forEach(System.out::println);
    }
    private void filtrarSeriesPorTemporadaYEvaluacion() {
        System.out.println("¿Filtrar séries con cuántas temporadas? ");
        var totalTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("¿Con evaluación apartir de cuál valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repository.seriesPorTemparadaYEvaluacion(totalTemporadas, evaluacion);
        System.out.println("*** Series filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - evaluacion: " + s.getEvaluacion()));
    }

    private void buscarEpisodeosPorTitulo() {
        System.out.println("Escribe el nombre del episodeo");
        var nombre = teclado.nextLine();
        List<Episodeo> episodeos = repository.episodeosPorNombre(nombre);
        episodeos.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodeo %s Evaluacion %s \n",e.getSerie(),e.getTemporada(),e.getNumeroEpisodeo(),e.getEvaluacion()));
    }

    private void top5EpisodeosSerie() {
        buscarSeriePorTitulo();
        if (serieOptional.isPresent()){
            Serie serie = serieOptional.get();
            List<Episodeo> top5 = repository.top5Episodeos(serie);
            top5.forEach(e ->
                    System.out.printf(
                            "Serie: %s - Temporada %s - Episodeo %s - Evaluacion %s \n",
                            e.getSerie(),e.getTemporada(),e.getNumeroEpisodeo(),e.getEvaluacion()));
        }
        else{
            System.out.println("Serie no encontrado");
        }

    }

}
