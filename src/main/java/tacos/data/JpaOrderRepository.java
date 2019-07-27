package tacos.data;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tacos.Order;

public interface JpaOrderRepository extends CrudRepository<Order, Long>{
	List<Order> findByDeliveryZip(String deliveryZip);
	List<Order> readOrderByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
	@Query("Order o where o.deliveryCity='Seattle'")
	List<Order> readOrdersDeliveredInSeattle();
}
