package br.com.swapi.commons.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

/**
 * Representa as entidades.
 */
@QueryEntity
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id"})
@SuperBuilder
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = -6536341488318207281L;
	
	@Id
	@ApiModelProperty(notes = "O identificado do registro", example = "5dc4c9734e9b1214ed7a9e3a")
	private String id;
	
}
