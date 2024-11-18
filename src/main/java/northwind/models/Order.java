package northwind.models;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Order {

	private Long customerId;

	private Address address;

	private List<OrderItem> orderItems;

	private Long restaurantId;

	@Override
	public String toString() {
		return "Order for customer [customerId=" + customerId + ",from restaurantId=" + restaurantId + "]";
	}
	
	
}
