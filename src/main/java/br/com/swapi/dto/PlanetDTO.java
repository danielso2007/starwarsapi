package br.com.swapi.dto;

import java.util.List;

import br.com.swapi.commons.dto.BaseAuditDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
@Builder
public class PlanetDTO extends BaseAuditDTO {

	private String name;
	private List<Object> climate;
	private List<Object> terrain;
	private Integer films;

}
