package lastcoder.service;

import org.springframework.stereotype.Service;

import lastcoder.dao.UserRepository;
import lastcoder.dto.AuthInfo;
import lastcoder.dto.SiteUser;

@Service
public class AuthService {

	private SiteUser siteUser;
	
	private UserRepository userRepository;

	private WrongIdPasswordException wrongIdPasswordException;
	
	public void setMemberDao(SiteUser siteUser) {
		this.siteUser = siteUser;
	}
	 
	
	public AuthInfo authenticate(String username, String password) {
		SiteUser siteUser = userRepository.findBySiteUserUsernameAndPassword(username, password);
		try {
			wrongIdPasswordException.WrongIdPasswordException(siteUser);
		}catch(Exception e) {
			System.out.println("계정 정보가 없습니다.");
		}
		return new AuthInfo(siteUser.getUsername(), siteUser.getEmail());
	}
}

