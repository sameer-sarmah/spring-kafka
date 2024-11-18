package northwind.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Recipe {
	private Long restaurantId;
	private Long recipeId;
	private String name;
	
	private String description;

	private double unitPrice;
}
