package com.urna.enuns;

public enum TipoPessoa {
    LIDER_SECAO("Lider Seçao"),
    CANDIDATO("Candidato");


    private String descricao;

    TipoPessoa(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
