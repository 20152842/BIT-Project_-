package lastcoder.dto;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserChangePwForm {

	@NotEmpty(message = "현재 비밀번호는 필수항목입니다.")
	@Length(min = 8, max = 50)
	private String password;

	@NotEmpty(message = "새비밀번호 입력은 필수항목입니다.")
	@Length(min = 8, max = 50)
	private String password1;

	@NotEmpty(message = "새비밀번호 확인은 필수항목입니다.")
	@Length(min = 8, max = 50)
	private String password2;


}
