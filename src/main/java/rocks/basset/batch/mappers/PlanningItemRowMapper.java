package rocks.basset.batch.mappers;

import org.springframework.jdbc.core.RowMapper;
import rocks.basset.batch.domain.PlanningItem;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanningItemRowMapper implements RowMapper<PlanningItem> {

    @Override
    public PlanningItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PlanningItem.builder()
                .libelleFormation(rs.getString(1))
                .dateDebutSeance(rs.getDate(2).toLocalDate())
                .dateFinSeance(rs.getDate(3).toLocalDate())
                .build();
    }
}
