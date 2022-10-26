package rocks.basset.batch.mappers;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Formation;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FormationItemPreparedStatementSetter implements ItemPreparedStatementSetter<Formation> {
    public static final String FORMATION_INSERT_QUERY = "INSERT INTO formations(code, libelle, descriptif) VALUES(?,?,?);";

    @Override
    public void setValues(Formation formation, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, formation.getCode());
        preparedStatement.setString(2, formation.getLibelle());
        preparedStatement.setString(3, formation.getDescriptif());
    }
}
