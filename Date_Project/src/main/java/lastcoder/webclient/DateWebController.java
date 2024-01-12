package lastcoder.webclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DateWebController {

	private DateWebService dateWebService;

	@Autowired
	public DateWebController(DateWebService dateWebService) {
		this.dateWebService = dateWebService;
	}

	@GetMapping("/webapp/posts/")
	public String DateWebClient(Model model) {
		DateWebRepository dateWebRepository = new DateWebRepository();
		System.out.println("======================================================================");
		DateWebRepository response = dateWebService.getFirstTodoTest("webapp/posts/", dateWebRepository);
		System.out.println("======================================================================" + response);
		System.out.println("=================================");
		System.out.println(response.getKind());
		System.out.println(response.getQty());
		model.addAttribute("kind", response.getKind());
		model.addAttribute("qty", response.getQty());
		return "hello";
	}
	

}
