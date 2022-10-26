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
public class Seance {
    private Integer idFormateur;
    private String codeFormation;
    private LocalDate dateDebut;
    private LocalDate dateFin;
}
