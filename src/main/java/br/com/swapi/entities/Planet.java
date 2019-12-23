package br.com.swapi.entities;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

import com.querydsl.core.annotations.QueryEntity;

import br.com.swapi.commons.entity.BaseAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@QueryEntity
@Document
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true, of = { "name" })
@ToString(of = { "name" })
@SuperBuilder
public class Planet extends BaseAudit {

	private static final long serialVersionUID = -6501107036834466748L;

	private String name;
	private List<Object> climate;
	private List<Object> terrain;
	private Integer films;

}
