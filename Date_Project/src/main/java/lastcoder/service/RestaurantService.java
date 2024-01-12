package lastcoder.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import lastcoder.dao.RestaurantRepository;
import lastcoder.dto.Restaurant;
import lombok.RequiredArgsConstructor;


@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;

	Logger logger = LoggerFactory.getLogger("com.mysite.sbb.user.RestaurantService");

	public ModelAndView RestaurantDB(String restaurantname, Double restaurantloxx, Double restaurantloxy) {

		ModelAndView mv = new ModelAndView();

		List<Restaurant> tmp_list = restaurantRepository
				.findByRestaurantRestaurantNameAndRestaurantloxxAndRestaurantloxy();

		String rename = restaurantname;
		Double reloxx = restaurantloxx;
		Double reloxy = restaurantloxy;
//		String url = "find_id";

		for (Restaurant restaurant : tmp_list) {
			if (restaurant.getRestaurantname().equals(rename) && restaurant.getRestaurantloxx().equals(reloxx)
					&& restaurant.getRestaurantloxy().equals(reloxy))
				mv.addObject("name", rename);
			mv.addObject("lox_x", reloxx);
			mv.addObject("lox_y", reloxy);
			mv.setViewName("tmp_find_id");
			break;
		}
		return mv;
	}

	public void hello2(Restaurant restaurant) {
		logger.info("Restaurant Service진입");
		restaurant.getRestaurantid();
		restaurant.getRestaurantname();
	}

}
