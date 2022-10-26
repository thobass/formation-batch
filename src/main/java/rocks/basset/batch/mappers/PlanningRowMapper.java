package rocks.basset.batch.mappers;

import org.springframework.jdbc.core.RowMapper;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Planning;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanningRowMapper implements RowMapper<Planning> {
    @Override
    public Planning mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Planning.builder()
                .formateur(
                        Formateur.builder()
                                .id(rs.getInt("id"))
                                .nom(rs.getString("nom"))
                                .prenom(rs.getString("prenom"))
                                .adresseEmail(rs.getString("adresse_email"))
                                .build()
                )
                .build();
    }
}
