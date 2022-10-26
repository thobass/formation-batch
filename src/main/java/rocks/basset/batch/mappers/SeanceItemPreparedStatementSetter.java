package rocks.basset.batch.mappers;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Seance;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SeanceItemPreparedStatementSetter implements ItemPreparedStatementSetter<Seance> {
    public static final String SEANCES_INSERT_QUERY = "INSERT INTO seances(code_formation, id_formateur, date_debut, date_fin) VALUES(?,?,?,?);";

    @Override
    public void setValues(Seance seance, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, seance.getCodeFormation());
        preparedStatement.setInt(2, seance.getIdFormateur());
        preparedStatement.setDate(3, Date.valueOf(seance.getDateDebut()));
        preparedStatement.setDate(4, Date.valueOf(seance.getDateFin()));
    }
}
