package lastcoder.service;

import lastcoder.dto.SiteUser;

public class WrongIdPasswordException {

	
	public void WrongIdPasswordException(SiteUser siteUser) throws Exception {
		if(siteUser.equals(null)) {
			throw new Exception();
		}
	}
}
