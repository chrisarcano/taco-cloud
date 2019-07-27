package tacos.data;

import org.springframework.data.repository.CrudRepository;

import tacos.Taco;

public interface JpaTacoRepository extends CrudRepository<Taco, Long> {}
