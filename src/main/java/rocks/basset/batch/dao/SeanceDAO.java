package rocks.basset.batch.dao;

import rocks.basset.batch.domain.PlanningItem;

import java.util.List;

public interface SeanceDAO {
    int count();

    List<PlanningItem> getByFormateurId(final Integer formateurId);
}
