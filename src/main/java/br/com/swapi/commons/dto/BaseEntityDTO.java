package br.com.swapi.commons.dto;

import br.com.swapi.commons.type.BaseTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id" })
@SuperBuilder
public class BaseEntityDTO implements BaseTypeDTO {

	private String id;

}
