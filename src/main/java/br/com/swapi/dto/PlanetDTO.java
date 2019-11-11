package br.com.swapi.dto;

import java.util.List;

import br.com.swapi.commons.dto.BaseAuditDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
public class PlanetDTO extends BaseAuditDTO {

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
