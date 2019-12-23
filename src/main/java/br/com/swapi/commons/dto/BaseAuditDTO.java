package br.com.swapi.commons.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * Representa as entidades com auditoria.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class BaseAuditDTO extends BaseEntityDTO {

	@ApiModelProperty(notes = "A data de criação do registro. Não será permitido criar ou atualizar", example = "2019-11-08T12:00:00.212Z")
	private Date createdAt;
	@ApiModelProperty(notes = "O login do criado do registro. Não será permitido criar ou atualizar", example = "system")
	private String creator;
	@ApiModelProperty(notes = "A data de atualização do registro. Não será permitido criar ou atualizar", example = "2019-11-08T12:00:00.212Z")
	private Date updatedAt;
	@ApiModelProperty(notes = "O login do atualizador do registro. Não será permitido criar ou atualizar", example = "system")
	private String updater;

}
