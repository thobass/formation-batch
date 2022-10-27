package rocks.basset.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import rocks.basset.batch.dao.FormationDAO;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Formation;
import rocks.basset.batch.listeners.ChargementFormateursStepListener;
import rocks.basset.batch.listeners.ChargementFormationsStepListener;
import rocks.basset.batch.mappers.FormateurItemPreparedStatementSetter;
import rocks.basset.batch.mappers.FormationItemPreparedStatementSetter;

import javax.sql.DataSource;

import static rocks.basset.batch.mappers.FormateurItemPreparedStatementSetter.FORMATEURS_INSERT_QUERY;
import static rocks.basset.batch.mappers.FormationItemPreparedStatementSetter.FORMATION_INSERT_QUERY;

@Configuration
@Slf4j
public class ChargementFormationsStepConfig {

    @Bean
    @StepScope
    public StaxEventItemReader<Formation> formationItemReader(
            @Value("#{jobParameters['formationsFile']}") Resource inputFile
    ){
        return new StaxEventItemReaderBuilder<Formation>().name("formationItemReader")
                .resource(inputFile)
                .addFragmentRootElements("formation")
                .unmarshaller(formationMarshaller())
                .build();
    }

    @Bean
    public Jaxb2Marshaller formationMarshaller(){
        Jaxb2Marshaller bean = new Jaxb2Marshaller();
        bean.setClassesToBeBound(Formation.class);
        return bean;
    }

    @Bean
    public JdbcBatchItemWriter<Formation> formationItemWriter(final DataSource datasource){
        return new JdbcBatchItemWriterBuilder<Formation>()
                .dataSource(datasource)
                .sql(FORMATION_INSERT_QUERY)
                .itemPreparedStatementSetter(new FormationItemPreparedStatementSetter())
                .build();
    }

    @Bean
    public Step chargementFormationsStep(final StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("chargementFormationsStep")
                .<Formation, Formation>chunk(10)
                .reader(formationItemReader(null))
                .writer(formationItemWriter(null))
                .listener(chargementFormationsStepListener())
                .build();
    }

    @Bean
    public StepExecutionListener chargementFormationsStepListener() {
        return new ChargementFormationsStepListener();
    }
}
