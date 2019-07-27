package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Ingredient;

public interface JpaIngredientRepository extends CrudRepository<Ingredient, String>{
	
}
