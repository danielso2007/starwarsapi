package br.com.swapi.entities;

import br.com.swapi.commons.entity.BaseAudit;
import com.querydsl.core.annotations.QueryEntity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

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
