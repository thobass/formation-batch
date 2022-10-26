package rocks.basset.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanningItem {
    private String libelleFormation;
    private String descriptifFormation;
    private LocalDate dateDebutSeance;
    private LocalDate dateFinSeance;
}
