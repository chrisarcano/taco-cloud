package tacos.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import lombok.extern.slf4j.Slf4j;
import tacos.Taco;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/taco")
@SessionAttributes("order")
public class DesignTacoController {
	private final IngredientRepository ingredientRepository;
	private final TacoRepository tacoRepository;
	
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	@ModelAttribute(name = "taco")
	public Taco design() {
		return new Taco();
	}
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
		this.ingredientRepository = ingredientRepository;
		this.tacoRepository = tacoRepository;
	}
	@GetMapping
	public String showDesignForm(Model model) {
		initialize(model, new Taco());
		return "taco";
	}
	@PostMapping
	public String processDesign(@Valid Taco taco, Errors errors, Model model, @ModelAttribute Order order) {
		if(errors.hasErrors()) {
			initialize(model, taco);
			return "taco";
		}
		log.info("Processing taco: " + taco);
		order.getTacos().add(taco);
		tacoRepository.save(taco);
		return "redirect:/orders/current";
	}
	private void initialize(Model model, Taco taco) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepository.findAll().forEach(i -> ingredients.add(i));
		Type[] types = Ingredient.Type.values();
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), ingredients.stream().filter(i -> i.getType() == type).collect(Collectors.toList()));
		}
		model.addAttribute("taco", taco);
	}
	
}
