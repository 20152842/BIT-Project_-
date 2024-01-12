package lastcoder.dto;



import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserFindPwForm {

	
	@NotEmpty(message = "이메일은 필수항목입니다.")
	@Email
	private String email;
	
	@NotEmpty(message = "아이디는 필수항목입니다.")
	@Column(name = "USERNAME")
	private String username;

}
