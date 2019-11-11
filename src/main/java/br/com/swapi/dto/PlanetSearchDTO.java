package br.com.swapi.dto;

import br.com.swapi.commons.dto.BaseEntityDTO;
import br.com.swapi.commons.type.BaseSearchTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
public class PlanetSearchDTO extends BaseEntityDTO implements BaseSearchTypeDTO {

	private String name;
	private String climate;
	private String terrain;
	private Integer films;

}
