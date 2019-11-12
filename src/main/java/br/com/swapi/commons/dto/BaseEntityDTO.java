package br.com.swapi.commons.dto;

import br.com.swapi.commons.type.BaseTypeDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Representa as entidades.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id" })
@SuperBuilder
public class BaseEntityDTO implements BaseTypeDTO {

	@ApiModelProperty(notes = "O identificado do registro", example = "5dc4c9734e9b1214ed7a9e3a")
	private String id;

}
