package lastcoder.dto;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SiteUser implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	@JoinColumn(unique = true, name = "username_id")
	private SiteUserRoleSet siteUserRoleSet;
	

	@Column(unique = true)
	private String username;

	private String name;

	private Integer gender;

	private String password;

	public boolean matchPassword(String password) {
		return this.password.equals(password);
	}
	
	@Column(unique = true)
	private String email;

	@Column(unique = true)
	@Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$")
	private String phon;

	@Column(columnDefinition = "Integer", nullable = false)
	private Integer email_auth = 0;

	@Column(columnDefinition = "varchar(50)", nullable = false)
	private String email_key = "";

	
}