package rocks.basset.batch.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Formateur {
    private Integer id;
    private String nom;
    private String prenom;
    private String adresseEmail;
}
