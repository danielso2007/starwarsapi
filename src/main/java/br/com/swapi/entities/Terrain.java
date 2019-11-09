package br.com.swapi.entities;

import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

import br.com.swapi.commons.entity.BaseAudit;
import br.com.swapi.enums.EntityStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@QueryEntity
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = {"name"})
@ToString(of = {"name"})
public class Terrain extends BaseAudit {

	private static final long serialVersionUID = 3201325419655057909L;

	private String name;
	private EntityStatusEnum status;

}
