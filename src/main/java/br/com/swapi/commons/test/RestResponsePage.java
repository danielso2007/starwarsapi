package br.com.swapi.commons.test;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Classe usada nos testes de controllers. <b>Usar apenas em testes.</b>
 *
 * @param <T> O Tipo de classe retornada pela paginação.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "content", "pageable", "totalElements", "totalPages", "last", "first", "numberOfElements", "sort",
		"size", "number", "empty" })
public class RestResponsePage<T> extends PageImpl<T> {

	private static final long serialVersionUID = -2975974509851416609L;

	@JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
	public RestResponsePage(@JsonProperty("content") List<T> content, @JsonProperty("pageable") JsonNode pageable,
			@JsonProperty("totalElements") Integer totalElements, @JsonProperty("totalPages") Integer totalPages,
			@JsonProperty("last") Boolean last, @JsonProperty("first") Boolean first,
			@JsonProperty("numberOfElements") Integer numberOfElements, @JsonProperty("sort") JsonNode sort,
			@JsonProperty("size") Integer size, @JsonProperty("number") Integer number,
			@JsonProperty("empty") Boolean empty) {

		super(content, PageRequest.of(number, size), totalElements);
	}

	public RestResponsePage(List<T> content, Pageable pageable, long total) {
		super(content, pageable, total);
	}

	public RestResponsePage(List<T> content) {
		super(content);
	}

	public RestResponsePage() {
		super(new ArrayList<>());
	}
}