package br.com.swapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import br.com.swapi.commons.dto.BaseEntityDTO;
import br.com.swapi.commons.type.BaseSearchTypeDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
@SuperBuilder
@ApiModel(description = "Modelo para o filtro de pesquisa de planetas")
public class PlanetSearchDTO extends BaseEntityDTO implements BaseSearchTypeDTO {

	@ApiModelProperty(notes = "O nome do planeta", example = "alderaan")
	private String name;

	@ApiModelProperty(notes = "O clima do planeta", example = "tropical", position = 1)
	private String climate;

	@ApiModelProperty(notes = "O terreno do planeta", example = "jungle", position = 2)
	private String terrain;

	@ApiModelProperty(notes = "A quantidade de filmes que o planeta apareceu no universo Starwars", example = "2", position = 3)
	private Integer films;

}
