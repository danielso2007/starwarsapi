package br.com.swapi.commons.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BaseAuditDTO extends BaseEntityDTO {

	private Date createdAt;
	private String creator;
	private Date updatedAt;
	private String updater;

}