package rocks.basset.batch.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import rocks.basset.batch.domain.PlanningItem;
import rocks.basset.batch.mappers.PlanningItemRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class SeanceDAOImpl implements SeanceDAO {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    private static final String QUERY = "select f.libelle, s.date_debut, s.date_fin" +
            " from formations f join seances s on f.code=s.code_formation" +
            " where s.id_formateur=:formateur" +
            " order by s.date_debut ";


    @Override
    public int count() {
        return jdbcTemplate.queryForObject("select count(*) from seances", new HashMap<>(),Integer.class);
    }

    @Override
    public List<PlanningItem> getByFormateurId(Integer formateurId) {
        Map<String, Integer> params = new HashMap<>();
        params.put("formateur", formateurId);

        return jdbcTemplate.query(QUERY, params, new PlanningItemRowMapper());
    }
}
