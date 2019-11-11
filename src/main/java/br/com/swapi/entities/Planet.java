package br.com.swapi.entities;

import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

import br.com.swapi.commons.entity.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@QueryEntity
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
@Builder
public class Planet extends BaseAudit {

	private static final long serialVersionUID = -6501107036834466748L;

	@NotBlank(message = "Nome é obrigatório")
	@NotNull(message = "Nome não pode ser nulo")
	@Size(min = 2, max = 20, message = "Nome deve ter entre 2 e 20 caracteres")
	private String name;
	@NotNull(message = "Clima é obrigatório")
	@Size(min = 1, message = "Pelo menos um clima deve ser informado")
	private List<Object> climate;
	@NotNull(message = "Terreno é obrigatório")
	@Size(min = 1, message = "Pelo menos um terreno deve ser informado")
	private List<Object> terrain;
	@Min(value = 0, message = "Quantidades de filmes não pode ser menor que 0")
	@Max(value = 10, message = "Quantidades de filmes não pode ser maior que 10")
	private Integer films;

}
