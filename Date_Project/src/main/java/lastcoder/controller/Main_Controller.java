package lastcoder.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

import lastcoder.dao.RestaurantRepository;
import lastcoder.dao.UserRepository;
import lastcoder.dto.Answer;
import lastcoder.dto.AnswerForm;
import lastcoder.dto.AuthInfo;
import lastcoder.dto.Login_Process_Object;
import lastcoder.dto.Question;
import lastcoder.dto.QuestionForm;
import lastcoder.dto.Restaurant;
import lastcoder.dto.SiteUser;
import lastcoder.dto.UserChangePwForm;
import lastcoder.dto.UserCreateForm;
import lastcoder.dto.UserFindIdForm;
import lastcoder.dto.UserFindPwForm;
import lastcoder.service.AnswerService;
import lastcoder.service.AuthService;
import lastcoder.service.QuestionService;
import lastcoder.service.RestaurantService;
import lastcoder.service.UserService;

@Controller
@RequestMapping("")
public class Main_Controller {

	@RequestMapping("/main")
	public String map() {
		return "/main";
	}

}

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

@Controller
@RequestMapping("/answer")
class AnswerController {

	private QuestionService questionService;
	private AnswerService answerService;
	private UserService userService;

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm,
			BindingResult bindingResult, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		if (bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		Answer answer = this.answerService.create(question, answerForm.getContent(), siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/update/{id}")
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		answerForm.setContent(answer.getContent());
		return "answer_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/update/{id}")
	public String answerModify(@Valid AnswerForm answerForm, BindingResult bindingResult,
			@PathVariable("id") Integer id, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.answerService.modify(answer, answerForm.getContent());
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String answerDelete(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		if (!answer.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerVote(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.answerService.vote(answer, siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}


	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String answerRead(Principal principal, @PathVariable("id") Integer id) {
		Answer answer = this.answerService.getAnswer(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.answerService.vote(answer, siteUser);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
}
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
@RequestMapping("/question")
@Controller
class QuestionController {

	private QuestionService questionService;
	private UserService userService;

	@RequestMapping("/list/{paging}")
	public String list(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "kw", defaultValue = "") String kw) {
		Page<Question> paging = questionService.getList(page, kw);
		model.addAttribute("paging", paging);
		model.addAttribute("kw", kw);
		return "question_list";
		// 템플릿을 사용하기 때문에 기존에 사용했던 @ResponseBody 애너테이션은 필요없으므로 삭제한다.
		// 그리고 list 메서드에서 question_list.html 템플릿 파일의 이름인 "question_list"를 리턴한다.
		// @RequiredArgsConstructor 애너테이션으로 questionRepository 속성을 포함하는 생성자를 생성하였다.
		// @RequiredArgsConstructor는 롬복이 제공하는 애너테이션으로 final이 붙은 속성을 포함하는 생성자를 자동으로 생성하는
		// 역할을 한다.
		// 롬복의 @Getter, @Setter가 자동으로 Getter, Setter 메서드를 생성하는 것과 마찬가지로
		// @RequiredArgsConstructor는 자동으로 생성자를 생성한다.
		// 따라서 스프링 의존성 주입 규칙에 의해 questionRepository 객체가 자동으로 주입된다.
	}

	@RequestMapping("/detail/{id}")
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		Question question = this.questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail";
		// 요청 URL http://localhost:8080/question/detail/2의 숫자 2처럼 변하는 id 값을 얻을 때에는
		// 위와 같이 @PathVariable 애너테이션을 사용해야 한다.
		// 이 때 @RequestMapping(value = "/question/detail/{id}")
		// 에서 사용한 id와 @PathVariable("id")의 매개변수 이름이 동일해야 한다.

	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String questionCreate(QuestionForm questionForm) {
		return "question_form";

	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.create(questionForm.getSubject(), questionForm.getContent(), siteUser);
		return "redirect:/question/list"; // 질문 저장후 질문목록으로 이동
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		return "question_form";
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal,
			@PathVariable("id") Integer id) {
		if (bindingResult.hasErrors()) {
			return "question_form";
		}
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		this.questionService.modify(question, questionForm.getSubject(), questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		if (!question.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.questionService.delete(question);
		return "redirect:/";
	}

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String questionVote(Principal principal, @PathVariable("id") Integer id) {
		Question question = this.questionService.getQuestion(id);
		SiteUser siteUser = this.userService.getUser(principal.getName());
		this.questionService.vote(question, siteUser);
		return String.format("redirect:/question/detail/%s", id);
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
@Controller
@RequestMapping("/")
// @RestController
class RestaurantController {

	@Autowired
	RestaurantService restaurantService;

	@Autowired
	RestaurantRepository restaurantRepository;

	Logger logger = LoggerFactory.getLogger("com.project.controller.RestaurantController");

	List<Restaurant> reclass = new ArrayList<Restaurant>();

	@GetMapping("map")
	public ModelAndView restaurnatdata() {
		ModelAndView mv = new ModelAndView();
		logger.info("============> 여기냐?");
		List<Restaurant> restaurant = new ArrayList<>();
		restaurant = restaurantRepository.findAll();

		mv.addObject("name", restaurant);
		mv.setViewName("map");

		return mv;
	}

}

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
@Controller
@RequestMapping("/user")
class UserController {

	Logger logger = LoggerFactory.getLogger("com.mysite.sbb.user.UserController");

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthService authService;
	
	

//		@Autowired
//		WebClient webClient;

	@GetMapping("/login")
	public String login() {
//		logger.info("===========>get login");
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		System.out.println("여기에요=================+++++++++++++++++" + principal);
		return "login_form";

	}



//	@PostMapping("/login_success")
//	public String loginSuccess(HttpServletRequest request, @Param("username") String username,
//			@Param("password") String password) {
//
//		AuthInfo authInfo = new AuthInfo(username, password);
//
//		HttpSession session = request.getSession();
//		session.setAttribute("user", authInfo);
//
//		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//
//		if (principal instanceof UserDetails) {
//			System.out.println(((UserDetails) principal).getUsername());
//		} else {
//			System.out.println(principal.toString());
//		}
//		System.out.println("여기도 나오나요??????+++++++++++++++++++++++++++++++++++" + principal);
//		System.out.println("여기도 나오나요???+++++++++++++++++++++++++++++++++++" + session.getAttribute("user"));
//		return "redirect:/";
//	}

	@PostMapping("/user/logout")
	public String logOutProcess(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		session.invalidate();
		return "/main";
	}

	@GetMapping("/signup")
	public String signup(UserCreateForm userCreateForm) {
		return "signup_form";
	}
	
	@PostMapping("/signup")
	public String signup(Model model, @Valid UserCreateForm userCreateForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "signup_form";
		}

		if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
			return "signup_form";
		}

		try {

			userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getGender(),
					userCreateForm.getPassword1(), userCreateForm.getName(), userCreateForm.getPhon());
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", "이미 등록된 사용자입니다.");
			model.addAttribute("error_msg", "이미 등록된 사용자입니다.");
			return "signup_form";
		} catch (Exception e) {
			e.printStackTrace();
			bindingResult.reject("signupFailed", e.getMessage());
			return "signup_form";
		}
		logger.info("===========> post singnup");
		return "redirect:/";
	}
//		@PostMapping("/login")
//		public String login(@Valid @ModelAttribute UserLoginForm userLoginForm, BindingResult bindingResult, HttpServletResponse response) {
//			logger.info("===========>post login");
//	    	System.out.println("===========>post login");
//			if (bindingResult.hasErrors()) {
//		    	System.out.println("===========>post login");
//
//		        return "/login_form";
//		    }
//
//		    SiteUser loginMember = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
//		    if (loginMember == null) {
//		        bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//		        return "/login_form";
//		    }
//
//		    //쿠키에 시간 정보를 주지 않으면 세션 쿠키가 된다. (브라우저 종료시 모두 종료)
//		    Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
//		    response.addCookie(idCookie);
//		    
//		    return "redirect:/";
//		}

	@GetMapping("/findid")
	public String find_id(UserFindIdForm userFindIdForm) {
		return "find_id";
	}

	@PostMapping("/findid")
	public ModelAndView find_id(@Valid UserFindIdForm userFindIdForm, BindingResult bindingResult) {
		ModelAndView mv = new ModelAndView();
		try {

			mv = userService.find_id(userFindIdForm.getEmail(), userFindIdForm.getName());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return mv;
	}

	@GetMapping("/findpw")
	public String find_pw(UserFindPwForm userFindPwForm) {
		return "find_pw";
	}

	@PostMapping("/findpw")
	public String find_pw(@Valid UserFindPwForm userFindPwForm, BindingResult bindingResult) {

		try {
			userService.sendMail(userService.find_pw(userFindPwForm.getEmail(), userFindPwForm.getUsername()));
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

		return "redirect:/user/login";
	}

	@RequestMapping("/registerEmail")
	public ModelAndView registerEmail(@RequestParam String email, @RequestParam String email_key) {
		ModelAndView mv = new ModelAndView();

		if ((userRepository.CountBySiteUserUserName(email)).intValue() == 1) {
			List<SiteUser> tmp_list = userRepository.findAll();
			for (SiteUser user : tmp_list) {

				if ((user.getEmail()).equals(email) && (user.getEmail_key()).equals(email_key)) {
					String pwd = userService.resetPwd(user.getEmail(), user.getEmail_key());
					mv.addObject("pwd", pwd);
					mv.setViewName("showPassword");

					return mv;
				}
			}

		}
		return mv;
	}

	@GetMapping("/change_pwd")
	public ModelAndView change_pw(HttpServletRequest request, UserChangePwForm userChangePwForm) {
		logger.info("change_pw ++++++++++++++++++++++++++++++");
		ModelAndView mv = new ModelAndView();
		HttpSession session = request.getSession();
		AuthInfo user = (AuthInfo) session.getAttribute("user");
		mv.addObject("user", user);
		mv.setViewName("change_pwd");
		return mv;
	}

//		@PostMapping("/change_pwd")
//		public String passwordEdit(Model model, UserChangePwForm form, BindingResult result,
//				@AuthenticationPrincipal SiteUser currentMember) {
//			if (result.hasErrors()) {
//				return "redirect:/login";
//			}
//
//			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//
//			if (!encoder.matches(form.getPassword(), currentMember.getPassword())) {
//				model.addAttribute("error", "현재 패스워드 불일치");
//				return "/change_pwd";
//			}
//
//			if (form.getPassword().equals(form.getPassword1())) {
//				model.addAttribute("error", "동일한 패스워드");
//				return "/change_pwd";
//			}
//
//			if (!form.getPassword1().equals(form.getPassword2())) {
//				model.addAttribute("error", "새 패스워드 불일치");
//				return "/change_pwd";
//			}
//
//			String encodedNewPwd = encoder.encode(form.getPassword2());
//			userService.changepw(currentMember.getUsername(), encodedNewPwd);
//			currentMember.setPassword(encodedNewPwd);
//			return "redirect:/";
//		}

//		@PostMapping("/change_pw")
//		public String change_pw(@ModelAttribute("password") String password, @ModelAttribute("password1") String password1, 
//				@ModelAttribute("password2") String password2, @ModelAttribute("username") String username,
//								BindingResult bindingResult)throws Exception {
//			ModelAndView mv = new ModelAndView();
//			mv.addObject("password", password);
//			mv.set("password", password);
//			mv.addObject("password", password);
//			
//			
//
//			if (bindingResult.hasErrors()) {
//				return "change_pw";
//			}
//
//			if (!password1.equals(password2)) {
//				bindingResult.rejectValue("password2", "passwordInCorrect", "2개의 패스워드가 일치하지 않습니다.");
//				return "change_pw";
//			}
//
//			try {
//				
//				userService.changepw(password1, username);
//
//			} catch (Exception e) {
//				e.printStackTrace();
//				bindingResult.reject("signupFailed", e.getMessage());
//				return "change_pw";
//			}
//
//			return "redirect:/";
//		}

//		public String deleteUser(@RequestParam("Email") String Email, RedirectAttributes redirectAttr,
//				SessionStatus sessionStatus) {
//
//			int result = userService.deleteMember(Email);
//
//			if (result > 0) {
//				redirectAttr.addFlashAttribute("msg", "성공적으로 회원정보를 삭제했습니다.");
//				SecurityContextHolder.clearContext();
//			} else
//				redirectAttr.addFlashAttribute("msg", "회원정보삭제에 실패했습니다.");
//
//			return "redirect:/";
//		}
}
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////
