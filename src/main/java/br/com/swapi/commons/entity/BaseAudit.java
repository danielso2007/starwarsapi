package br.com.swapi.commons.entity;

import java.util.Date;

import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@QueryEntity
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data()
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public abstract class BaseAudit extends BaseEntity {

	private static final long serialVersionUID = -6536341488318207281L;
	
	private Date createdAt;
	private String creator;
	private Date updatedAt;
	private String updater;

}
