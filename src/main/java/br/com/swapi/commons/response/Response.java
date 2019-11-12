package br.com.swapi.commons.response;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(of = { "data" })
@ApiModel(description = "Modelo de retorno da API")
public class Response<T> {

	@ApiModelProperty(notes = "Os dados do retorno", example = "Não há exemplos")
	private T data;
	@ApiModelProperty(notes = "A lista de erros de uma chamada", example = "errors: [Nome é obrigatório,Nome não pode ser nulo]")
	private List<String> errors;
	@ApiModelProperty(notes = "Indica se as mensagens de erro são de validação", example = "true ou false")
	private Boolean validation;

	public void addError(String error) {
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(error);
	}

}
