package lastcoder.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import lombok.Data;

@Entity
@Data
public class SiteUserRoleSet {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(columnDefinition = "Integer", nullable = false)
	private Integer roleset = -1;

	@OneToOne(mappedBy = "siteUserRoleSet")
	private SiteUser siteUser;
	
}
