package lastcoder.dao;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lastcoder.dto.SiteUser;




@Transactional
public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	Optional<SiteUser> findByUsername(String SiteUsername);

	Optional<SiteUser> findBypassword(String password);

	Optional<SiteUser> findByemail(String email);

	
	List<SiteUser> findAll();
	
	
	
	@Query(value = "SELECT * FROM SITE_SiteUser where SiteUsername = :SiteUsername and password = :password", nativeQuery = true)
	SiteUser findBySiteUserUsernameAndPassword(@Param("SiteUsername") String SiteUsername, @Param("password") String password);

//	public void save(SiteUser SiteUser);

	@Query(value = "SELECT count(*) FROM SITE_SiteUser where SiteUsername = :SiteUsername", nativeQuery = true)
	Integer CountBySiteUserUserName(@Param("SiteUsername") String SiteUsername);

	@Query(value = "SELECT * FROM SITE_SiteUser", nativeQuery = true)
	List<SiteUser> findBySiteUserFindIdFormEmailAndSiteUserFindIdFormName();

	@Query(value = "SELECT * FROM SITE_SiteUser", nativeQuery = true)
	List<SiteUser> findBySiteUserFindPwFormEmailAndSiteUserFindPwFormSiteUsername();

	@Query(value = "SELECT * FROM SITE_SiteUser", nativeQuery = true)
	List<SiteUser> findBySiteUserLoginFormSiteUsernameAndSiteUserLoginFormPassword();

//	 메일 인증시 mail_auth 값을 1로 바꿔 로그인 허용

	@Modifying
	@Query(value = "update SITE_SiteUser set email_auth=1 where email = :email and email_key = :email_key", nativeQuery = true)
	void UpdateEmail_Auth(@Param("email") String email, @Param("email_key") String email_key);

	@Modifying
	@Query(value = "update SITE_SiteUser set PASSWORD = :password where email = :email and email_key = :email_key", nativeQuery = true)
	void UpdatePassword(@Param("password") String password, @Param("email") String email,
			@Param("email_key") String email_key);

//	 메일 인증 없을시 0 반환
	@Query(value = "SELECT COUNT(*) from SITE_SiteUser WHERE email = :email and email_auth=1", nativeQuery = true)
	Long CountBySiteSiteUser(@Param("email") String email);
//
//	@Modifying
//	@Query(value = "update SITE_SiteUser set PASSWORD = :password", nativeQuery = true)
//	List<SiteUserChangePwForm> findBypasswordUpdateNewPassword(@Param("password") String password);

//	
//
//	

}
