package lastcoder.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor

public class RestaurantDto {

	private String restaurantname;
    private Double restaurantloxx;
    private Double restaurantloxy;
    
    
	public RestaurantDto(String restaurantname, Double restaurantloxx, Double restaurantloxy) {
		this.restaurantname = restaurantname;
		this.restaurantloxx = restaurantloxx;
		this.restaurantloxy = restaurantloxy;
	}
    

  

}
