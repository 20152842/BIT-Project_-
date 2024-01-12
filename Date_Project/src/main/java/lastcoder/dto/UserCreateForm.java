package lastcoder.dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserCreateForm {
    @Size(min = 3, max = 25)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
    @NotEmpty(message = "사용자ID는 필수항목입니다.")
    @Column(name = "USERNAME")
    private String username;

    @NotEmpty(message = "비밀번호는 필수항목입니다.")
    @Length(min = 8, max = 50)
    private String password1;

    @NotEmpty(message = "비밀번호 확인은 필수항목입니다.")
    @Length(min = 8, max = 50)
    private String password2;

    @NotEmpty(message = "이메일은 필수항목입니다.")
    @Email
    private String email;
    
    @NotEmpty(message = "전화번호는 필수항목입니다.")
    private String phon;
    
    @NotEmpty(message = "이름은 필수항목입니다.")
    private String name;
    
    @NotNull(message="성별 선택은 필수항목입니다.")
    private Integer gender;
}