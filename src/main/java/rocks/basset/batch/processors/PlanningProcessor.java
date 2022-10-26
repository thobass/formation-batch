package rocks.basset.batch.processors;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rocks.basset.batch.domain.Planning;
import rocks.basset.batch.domain.PlanningItem;
import rocks.basset.batch.mappers.PlanningItemRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PlanningProcessor implements ItemProcessor<Planning, Planning> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private static final String QUERY = "select f.libelle, s.date_debut, s.date_fin" +
            " from formations f join seances s on f.code=s.code_formation" +
            " where s.id_formateur=:formateur" +
            " order by s.date_debut ";

    @Override
    public Planning process(Planning planning) throws Exception {
        Map<String, Integer> params = new HashMap<>();
        params.put("formateur", planning.getFormateur().getId());

        List<PlanningItem> items = jdbcTemplate.query(QUERY, params, new PlanningItemRowMapper());
        planning.setSeances(items);
        return planning;
    }
}
