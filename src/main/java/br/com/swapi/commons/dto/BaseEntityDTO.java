package br.com.swapi.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = { "id" })
@ToString(of = { "id" })
public class BaseEntityDTO {

	private String id;

}
