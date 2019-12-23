package br.com.swapi.enums;

public enum EntityStatusEnum {

    INACTIVE("Inativo"),
    ACTIVE("Ativo");

    private final String description;

    EntityStatusEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
