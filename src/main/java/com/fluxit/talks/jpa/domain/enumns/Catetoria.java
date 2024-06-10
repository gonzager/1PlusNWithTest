package com.fluxit.talks.jpa.domain.enumns;

public enum Catetoria {
    DEPORTE("D"), MUSICA("M"), TECNOLOGIA("T"), HOGAR("H");

    private String codigo;

    private Catetoria(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }
}

