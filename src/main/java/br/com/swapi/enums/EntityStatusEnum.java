package br.com.swapi.enums;

public enum EntityStatusEnum {

	INACTIVE("Inativo"),
	ACTIVE("Ativo");
	
	EntityStatusEnum(String description) {
		this.description = description;
	}

	private final String description;

	public String getDescription() {
		return description;
	}
	
}
