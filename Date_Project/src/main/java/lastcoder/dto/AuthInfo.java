package lastcoder.dto;

import lombok.Data;

@Data
public class AuthInfo {
	private String username;
	private String email;
	
	public AuthInfo(String username, String email) {
		this.username = username;
		this.email = email;
	}

	
}