package br.com.swapi.commons.response;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {

	private T data;
	private List<String> errors = new ArrayList<>();
	private boolean validation = false;

	public void addError(String error) {
		getErrors().add(error);
	}

	public Response() {
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public List<String> getErrors() {
		if (this.errors == null) {
			this.errors = new ArrayList<>();
		}
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

	public boolean isValidation() {
		return validation;
	}

	public void setValidation(boolean validation) {
		this.validation = validation;
	}

}
