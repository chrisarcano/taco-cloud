package tacos.data;

import tacos.Design;

public interface TacoRepository {
	Design save(Design design);

	Iterable<Design> findAll();
}
