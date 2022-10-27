package rocks.basset.batch.processors;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import rocks.basset.batch.dao.SeanceDAO;
import rocks.basset.batch.domain.Planning;
import rocks.basset.batch.domain.PlanningItem;
import rocks.basset.batch.mappers.PlanningItemRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class PlanningProcessor implements ItemProcessor<Planning, Planning> {

    private final SeanceDAO seanceDAO;

    @Override
    public Planning process(Planning planning) throws Exception {

        planning.setSeances(seanceDAO.getByFormateurId(planning.getFormateur().getId()));
        return planning;
    }
}
