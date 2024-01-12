package lastcoder.service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import lastcoder.config.DataNotFoundException;
import lastcoder.dao.UserRepository;
import lastcoder.dto.SiteUser;
import lastcoder.dto.UserChangePwForm;
import lastcoder.email.EmailHandler;
import lastcoder.email.TempKey;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private PasswordEncoder passwordEncoder;

	Logger logger = LoggerFactory.getLogger("com.mysite.sbb.user.UserService");

	public void create(String username, String email, Integer gender, String password, String name, String phon) {
		SiteUser user = new SiteUser();
		user.setUsername(username);
		user.setName(name);
		user.setGender(gender);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));
		user.setPhon(phon);
		logger.info("===========> getArticleList() 메서드 진입");
		this.userRepository.save(user);
	}

	public ModelAndView find_id(String email, String name) {

		ModelAndView mv = new ModelAndView();

//		List<SiteUser> tmp_list = userRepository.findByUserFindIdFormEmailAndUserFindIdFormName();
		List<SiteUser> tmp_list = null;
		
		String submit_email = email;
		String submit_name = name;
		String url = "find_id";

		for (SiteUser user : tmp_list) {
			if (user.getEmail().equals(submit_email) && user.getName().equals(submit_name)) {
				mv.addObject("user", user);
				mv.addObject("url", url);
				mv.setViewName("tmp_find_id");
				break;
			}
		}
		return mv;
	}

//	public SiteUser find_pw(String email, String username) {
//		SiteUser user = new SiteUser();
//		user.setEmail(email);
//		user.setUsername(username);
//		this.userRepository.save(user);
//		return user;
//	}

	public SiteUser login(String username, String password) {
		logger.info("===========> getArticleList() 메서드 진입");
		ModelAndView mv = new ModelAndView();

		String submit_username = username;
		String submit_password = password;
		String url = "login_form";

//		List<SiteUser> tmp_list = userRepository.findByUserLoginFormUsernameAndUserLoginFormPassword();
		List<SiteUser> tmp_list = null;

		for (SiteUser user : tmp_list) {
			if (user.getUsername().equals(submit_username) && user.getPassword().equals(submit_password)) {

				return user;
			}
		}
		return null;
	}

	public SiteUser find_pw(String email, String username) {
		ModelAndView mv = new ModelAndView();

		String submit_email = email;
		String submit_username = username;
		String url = "find_pw";

//		List<SiteUser> tmp_list = userRepository.findByUserFindPwFormEmailAndUserFindPwFormUsername();
		List<SiteUser> tmp_list = null;

		for (SiteUser user : tmp_list) {
			if (user.getEmail().equals(submit_email) && user.getUsername().equals(submit_username)) {
				return user;
			}
		}
		return null;
	}

	public SiteUser getUser(String username) {
		Optional<SiteUser> siteUser = this.userRepository.findByUsername(username);
		if (siteUser.isPresent()) {
			return siteUser.get();
		} else {
			throw new DataNotFoundException("siteuser not found");
		}
	}

	public void sendMail(SiteUser siteUser) throws MessagingException, UnsupportedEncodingException {
		String email_key = new TempKey().getKey(30, false);
		siteUser.setEmail_key(email_key);

		EmailHandler sendMail = new EmailHandler(javaMailSender);
		sendMail.setSubject("인증메일입니다.");
		sendMail.setText("<h1>RunningGo 메일인증 </h1>" + "<br> 아래 이메일 인증을 눌러주세요."
				+ "<br><a href=http://localhost:8080/user/registerEmail?email=" + siteUser.getEmail() + "&email_key="
				+ email_key + " 'target='_blank'>이메일 인증 확인 </a>");

		sendMail.setFrom("fc0209@naver.com", "bit");
		sendMail.setTo(siteUser.getEmail());
		sendMail.send();

		userRepository.UpdateEmail_Auth(siteUser.getEmail(), email_key);

	}

	public String resetPwd(String email, String email_key) {
		String newPwd = new TempKey().getKey(8, false);
		String pwd = newPwd;
		userRepository.UpdatePassword(passwordEncoder.encode(newPwd), email, email_key);
		return pwd;
	}

	public Integer checkUserName(String username) {
		return userRepository.CountBySiteUserUserName(username);
	}

	public UserChangePwForm ch_pw(String password, String password1, String password2) {
		ModelAndView mv = new ModelAndView();

		String submit_password = password;
		String submit_newpassword1 = password1;
		String submit_newpassword2 = password2;
		String url = "change_pw";

//		List<UserChangePwForm> tmp_list = userRepository.findBypasswordUpdateNewPassword(submit_password);
		List<UserChangePwForm> tmp_list = null;

		for (UserChangePwForm user : tmp_list) {

			user.setPassword1(passwordEncoder.encode(password));

			if (user.getPassword1().equals(submit_newpassword1) && user.getPassword2().equals(submit_newpassword2)) {
//				this.userRepository.save(user);
				return user;
			}
		}
		return null;

	}

	public void changepw(String password, String username) {
		Optional<SiteUser> user = userRepository.findByUsername(username);
		System.out.println(user.get().getUsername() + " +++++++++++++++++++++");
		user.get().setPassword(passwordEncoder.encode(password));

		logger.info("===========> getArticleList() 메서드 진입");
//		this.userRepository.save(user);
	}
//	 @Transactional
//	    public void deleteMember() {
//	        Long loginMemberId = SecurityUtil.;
//	        if (loginMemberId == null) {
//	            throw new RuntimeException("로그인 유저 정보가 없습니다.");
//	        }
//	        userRepository.deleteById(loginMemberId);
//	    }

}
