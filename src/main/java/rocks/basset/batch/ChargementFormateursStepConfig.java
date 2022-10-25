package rocks.basset.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.listeners.ChargementFormateursStepListener;
import rocks.basset.batch.mappers.FormateurItemPreparedStatementSetter;

import javax.sql.DataSource;

import static rocks.basset.batch.mappers.FormateurItemPreparedStatementSetter.FORMATEURS_INSERT_QUERY;


@Configuration
@Slf4j
public class ChargementFormateursStepConfig {


    @Bean
    @StepScope
    public FlatFileItemReader<Formateur> formateurItemReader(
            @Value("file:#{jobParameters['formateursFile']}") final Resource inputFile
    ){
        return new FlatFileItemReaderBuilder<Formateur>()
                .name("FormateurItemReader")
                .resource(inputFile)
                .delimited()
                .delimiter(";")
                .names("id","nom","prenom","adresseEmail")
                .targetType(Formateur.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<Formateur> formateurItemWriter(final DataSource datasource){
        return new JdbcBatchItemWriterBuilder<Formateur>()
                .dataSource(datasource)
                .sql(FORMATEURS_INSERT_QUERY)
                .itemPreparedStatementSetter(new FormateurItemPreparedStatementSetter())
                .build();
    }

    @Bean
    public Step chargementFormateursStep(final StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("chargementFormateursStep")
                .<Formateur, Formateur>chunk(10)
                .reader(formateurItemReader(null))
                .writer(formateurItemWriter(null))
                .listener(chargementFormateursStepListener())
                .build();
    }

    @Bean
    public StepExecutionListener chargementFormateursStepListener() {
        return new ChargementFormateursStepListener();
    }
}
