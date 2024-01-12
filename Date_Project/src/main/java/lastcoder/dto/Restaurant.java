package lastcoder.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table
@Data

//@Builder
public class Restaurant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer restaurantid;

	@Column
	private String restaurantname;

	@Column
	private Double restaurantloxx;

	@Column
	private Double restaurantloxy;


	
	

}