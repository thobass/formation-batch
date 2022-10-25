package rocks.basset.batch.mappers;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import rocks.basset.batch.domain.Formateur;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormateurItemPreparedStatementSetter implements ItemPreparedStatementSetter<Formateur> {
    public static final String FORMATEURS_INSERT_QUERY = "INSERT INTO formateurs(id, nom, prenom, adresse_email) VALUES(?,?,?,?);";

    @Override
    public void setValues(Formateur formateur, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setInt(1, formateur.getId());
        preparedStatement.setString(2, formateur.getNom());
        preparedStatement.setString(3, formateur.getPrenom());
        preparedStatement.setString(4, formateur.getAdresseEmail());
    }
}
