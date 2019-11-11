package br.com.swapi.commons.response;

import java.util.ArrayList;
import java.util.List;

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
public class Response<T> {

	private T data;
	private List<String> errors;
	private Boolean validation;

	public List<String> addError(String error) {
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		this.errors.add(error);
		return this.errors;
	}

}
