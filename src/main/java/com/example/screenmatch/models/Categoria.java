package com.example.screenmatch.models;

public enum Categoria {
    ACCION("Action","Acción"),
    ROMANCE("Romance","Romance"),
    COMEDIA("Comedy", "Comedia"),
    DRAMA("Drama", "Drama"),
    CRIMEN("Crime", "Crimen");

    private final String categoriaOmdb;
    private final String categoriaEspain;

    Categoria(String CategoriaOmbd, String categoriaEspain) {
        this.categoriaOmdb = CategoriaOmbd;
        this.categoriaEspain = categoriaEspain;
    }

    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }
    public static Categoria fromEspain(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaEspain.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + text);
    }

}
