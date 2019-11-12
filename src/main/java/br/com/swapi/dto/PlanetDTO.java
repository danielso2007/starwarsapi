package br.com.swapi.dto;

import java.util.List;

import br.com.swapi.commons.dto.BaseAuditDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
@SuperBuilder
@ApiModel(description = "Modelo para criar um novo Planet")
public class PlanetDTO extends BaseAuditDTO {

	@NotBlank(message = "Nome é obrigatório")
	@NotNull(message = "Nome não pode ser nulo")
	@Size(min = 2, max = 20, message = "Nome deve ter entre 2 e 20 caracteres")
	@ApiModelProperty(notes = "O nome do planeta", example = "Alderaan", required = true, position = 0)
	private String name;
	@NotNull(message = "Clima é obrigatório")
	@Size(min = 1, message = "Pelo menos um clima deve ser informado")
	@ApiModelProperty(notes = "A lista de climas do planeta", example = "[\"temperate\", \"tropical\"]", required = true, position = 1)
	private List<Object> climate;
	@NotNull(message = "Terreno é obrigatório")
	@Size(min = 1, message = "Pelo menos um terreno deve ser informado")
	@ApiModelProperty(notes = "A lista de terrenos do planeta", example = "[\"jungle\", \"rainforests\"]", required = true, position = 2)
	private List<Object> terrain;
	@Min(value = 0, message = "Quantidades de filmes não pode ser menor que 0")
	@Max(value = 10, message = "Quantidades de filmes não pode ser maior que 10")
	@ApiModelProperty(notes = "A quantidade de filmes que o planeta apareceu no universo Starwars", example = "1", position = 3)
	private Integer films;

}
