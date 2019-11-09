package br.com.swapi.enums;

public enum EntityStatusEnum {

	INACTIVE("Inativo"),
	ACTIVE("Ativo");
	
	private EntityStatusEnum(String description) {
		this.description = description;
	}

	private String description;

	public String getDescription() {
		return description;
	}
	
}
