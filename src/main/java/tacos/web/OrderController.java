package tacos.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import tacos.Order;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {
	private TacoRepository tacoRepository;
	@Autowired
	public OrderController(TacoRepository tacoRepository) {
		this.tacoRepository = tacoRepository;
	}
	@GetMapping("/current")
	public String orderForm(Model model) {
		Order order = new Order();
		tacoRepository.findAll().forEach(d -> order.getDesigns().add(d));
		model.addAttribute("order", order);
		model.addAttribute("designs", order.getDesigns());
		return "orderForm";
	}
	@PostMapping
	public String processOrder(@Valid Order order, Errors errors) {
		if(errors.hasErrors()) {
			return "orderForm";
		}
		log.info("Order submitted: " + order);
		return "redirect:/";
	}
}
