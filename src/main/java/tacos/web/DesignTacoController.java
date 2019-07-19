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
import tacos.Design;
import tacos.Ingredient;
import tacos.Ingredient.Type;
import tacos.Order;
import tacos.data.IngredientRepository;
import tacos.data.TacoRepository;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("order")
public class DesignTacoController {
	/*private List<Ingredient> ingredients = Arrays.asList(
		new Ingredient("FLTO", "Flour Tortilla", Type.WRAP),
		new Ingredient("COTO", "Corn Tortilla", Type.WRAP),
		new Ingredient("GRBF", "Ground Beef", Type.PROTEIN),
		new Ingredient("CARN","Carnitas",Type.PROTEIN),
		new Ingredient("TMTO", "Diced Tomatoes", Type.VEGGIES),
		new Ingredient("LETC", "Lettuce", Type.VEGGIES),
		new Ingredient("CHED", "Cheddar", Type.CHEESE),
		new Ingredient("JACK", "Monterey Jack", Type.CHEESE),
		new Ingredient("SLSA", "Salsa", Type.SAUCE),
		new Ingredient("SRCR", "Sour Cream", Type.SAUCE)
	);*/
	private final IngredientRepository ingredientRepository;
	private final TacoRepository tacoRepository;
	@ModelAttribute(name = "order")
	public Order order() {
		return new Order();
	}
	@ModelAttribute(name = "design")
	public Design design() {
		return new Design();
	}
	@Autowired
	public DesignTacoController(IngredientRepository ingredientRepository, TacoRepository tacoRepository) {
		this.ingredientRepository = ingredientRepository;
		this.tacoRepository = tacoRepository;
	}
	@GetMapping
	public String showDesignForm(Model model) {
		initialize(model, new Design());
		return "design";
	}
	@PostMapping
	public String processDesign(@Valid Design design, Errors errors, Model model, @ModelAttribute Order order) {
		if(errors.hasErrors()) {
			initialize(model, design);
			return "design";
		}
		log.info("Processing design: " + design);
		order.getDesigns().add(design);
		tacoRepository.save(design);
		return "redirect:/orders/current";
	}
	private void initialize(Model model, Design design) {
		List<Ingredient> ingredients = new ArrayList<>();
		ingredientRepository.findAll().forEach(i -> ingredients.add(i));
		Type[] types = Ingredient.Type.values();
		for(Type type : types) {
			model.addAttribute(type.toString().toLowerCase(), ingredients.stream().filter(i -> i.getType() == type).collect(Collectors.toList()));
		}
		model.addAttribute("design", design);
	}
	
}
