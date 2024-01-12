package lastcoder.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import lastcoder.dto.Restaurant;
@Transactional
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	Restaurant findByrestaurantid(Long restaurantid);

	List<Restaurant>findByRestaurantloxxAndRestaurantloxy(Double restaurantloxx, Double restaurantloxy);
	List<Restaurant> findAll();
	String findByRestaurantname(String restaurantname);

	Double findByRestaurantloxx(Double restaurantloxx);

	Double findByRestaurantloxy(Double restaurantloxy);

	Restaurant findAllByRestaurantid(Integer restaurantid);
	

	@Query(value = "SELECT count(*) FROM Restaurant where Restaurant_id = :Restaurant_id", nativeQuery = true)
	Long CountByRestaurant_id(@Param("Restaurant_id") Long Restaurant_id);

	@Query(value = "SELECT * FROM Restaurant", nativeQuery = true)
	List<Restaurant> findByRestaurantRestaurantNameAndRestaurantloxxAndRestaurantloxy();

	
}
