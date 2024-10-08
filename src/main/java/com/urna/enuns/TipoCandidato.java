package com.urna.enuns;

public enum TipoCandidato {
    PRESIDENTE("Presidente da República"),
    GOVERNADOR("Governador do Estado"),
    SENADOR("Senador da República"),
    DEPUTADO_FEDERAL("Deputado Federal"),
    DEPUTADO_ESTADUAL("Deputado Estadual"),
    PREFEITO("Prefeito Municipal"),
    VEREADOR("Vereador Municipal");

    private String descricao;

    TipoCandidato(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
