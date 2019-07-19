package tacos.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import tacos.Design;
import tacos.Ingredient;

@Repository
public class JdbcTacoRepository implements TacoRepository {

	private JdbcTemplate jdbc;
	
	@Autowired
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public Design save(Design design) {
		/*long tacoId = saveTacoInfo(design);
		design.setId(tacoId);
		for(String ingredient : design.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}*/
		saveTacoInfo(design);
		return design;
	}
	
	private long saveTacoInfo(Design design) {
		design.setCreateAt(new Date());
		PreparedStatementCreator psc = new PreparedStatementCreatorFactory(
			"insert into Taco(name, createdAt) values (?, ?)", Types.VARCHAR, Types.TIMESTAMP
		).newPreparedStatementCreator(Arrays.asList(design.getName(), new Timestamp(design.getCreateAt().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int x = jdbc.update(psc, keyHolder);
		//return keyHolder.getKey().longValue();
		return 0;
	}
	
	private void saveIngredientToTaco(String ingredient, long tacoId) {
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) values(?, ?", tacoId, ingredient);	
	}

	@Override
	public Iterable<Design> findAll() {
		return jdbc.query("select id, name, createdAt from Taco", this::mapRowToTaco);
	}
	
	private Design mapRowToTaco(ResultSet rs, int rowNum) throws SQLException {
		//return new Design(new Long(rs.getLong("id")), rs.getString("name"), new ArrayList<Ingredient>(), new Date(rs.getDate("createdAt").getTime()));
		Design design = new Design();
		design.setId(rs.getLong("id"));
		design.setName(rs.getString("name"));
		design.setCreateAt(new Date(rs.getDate("createdAt").getTime()));
		return design;
	}
	
	

}
