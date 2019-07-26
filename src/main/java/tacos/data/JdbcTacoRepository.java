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

import tacos.Taco;
import tacos.Ingredient;

@Repository
public class JdbcTacoRepository implements TacoRepository {

	private JdbcTemplate jdbc;
	
	@Autowired
	public JdbcTacoRepository(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}
	
	@Override
	public Taco save(Taco taco) {
		long tacoId = saveTacoInfo(taco);
		taco.setId(tacoId);
		for(String ingredient : taco.getIngredients()) {
			saveIngredientToTaco(ingredient, tacoId);
		}
		return taco;
	}
	
	private long saveTacoInfo(Taco taco) {
		taco.setCreateAt(new Date());
		PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(
				"insert into Taco(name, createdAt) values (?, ?)", Types.VARCHAR, Types.TIMESTAMP
				);
		pscf.setReturnGeneratedKeys(true);
		PreparedStatementCreator psc = pscf.newPreparedStatementCreator(Arrays.asList(taco.getName(), new Timestamp(taco.getCreateAt().getTime())));
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbc.update(psc, keyHolder);
		return keyHolder.getKey().longValue();
	}
	
	private void saveIngredientToTaco(String ingredient, long tacoId) {
		jdbc.update("insert into Taco_Ingredients (taco, ingredient) values(?, ?)", tacoId, ingredient);	
	}

	@Override
	public Iterable<Taco> findAll() {
		return jdbc.query("select id, name, createdAt from Taco", this::mapRowToTaco);
	}
	
	private Taco mapRowToTaco(ResultSet rs, int rowNum) throws SQLException {
		//return new Design(new Long(rs.getLong("id")), rs.getString("name"), new ArrayList<Ingredient>(), new Date(rs.getDate("createdAt").getTime()));
		Taco design = new Taco();
		design.setId(rs.getLong("id"));
		design.setName(rs.getString("name"));
		design.setCreateAt(new Date(rs.getDate("createdAt").getTime()));
		return design;
	}
	
	

}
