package rocks.basset.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Formation;
import rocks.basset.batch.domain.Seance;
import rocks.basset.batch.listeners.ChargementFormationsStepListener;
import rocks.basset.batch.listeners.ChargementSeancesStepListener;
import rocks.basset.batch.mappers.FormationItemPreparedStatementSetter;
import rocks.basset.batch.mappers.SeanceItemPreparedStatementSetter;
import rocks.basset.batch.policies.SeanceSkipPolicy;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static rocks.basset.batch.mappers.FormationItemPreparedStatementSetter.FORMATION_INSERT_QUERY;
import static rocks.basset.batch.mappers.SeanceItemPreparedStatementSetter.SEANCES_INSERT_QUERY;

@Configuration
@Slf4j
public class ChargementSeancesStepConfig {

    @Bean
    @StepScope
    public FlatFileItemReader<Seance> seanceCsvItemReader(
            @Value("#{jobParameters['seancesFile']}") Resource inputFile
    ){
        return new FlatFileItemReaderBuilder<Seance>()
                .name("SeanceCsvItemReader")
                .resource(inputFile)
                .delimited()
                .delimiter(";")
                .names("codeFormation","idFormateur","dateDebut","dateFin")
                .fieldSetMapper(seanceFieldSetMapper(null))
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Seance> seanceTxtItemReader(
            @Value("#{jobParameters['seancesFile']}") Resource inputFile
    ){
        return new FlatFileItemReaderBuilder<Seance>()
                .name("SeanceTxtItemReader")
                .resource(inputFile)
                .fixedLength()
                .columns(new Range(1,16), new Range(17,20), new Range(25,32), new Range(37,44))
                .names("codeFormation","idFormateur","dateDebut","dateFin")
                .fieldSetMapper(seanceFieldSetMapper(null))
                .build();
    }

    @Bean
    public ConversionService conversionService(){
        DefaultConversionService dcs = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(dcs);
        dcs.addConverter(new Converter<String, LocalDate>() {

            @Override
            public LocalDate convert(final String input) {
                DateTimeFormatter df = DateTimeFormatter.ofPattern("ddMMyyyy");
                return LocalDate.parse(input, df);
            }
        });

        return dcs;
    }

    @Bean
    public FieldSetMapper<Seance> seanceFieldSetMapper(final ConversionService conversionService){
        BeanWrapperFieldSetMapper<Seance> bean = new BeanWrapperFieldSetMapper<Seance>();
        bean.setTargetType(Seance.class);
        bean.setConversionService(conversionService);
        return bean;
    }

    @Bean
    public JdbcBatchItemWriter<Seance> seanceItemWriter(final DataSource datasource){
        return new JdbcBatchItemWriterBuilder<Seance>()
                .dataSource(datasource)
                .sql(SEANCES_INSERT_QUERY)
                .itemPreparedStatementSetter(new SeanceItemPreparedStatementSetter())
                .build();
    }

    @Bean
    public SkipPolicy seanceSkipPolicy(){
        return new SeanceSkipPolicy();
    }

    @Bean
    public Step chargementSeancesCsvStep(final StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("chargementSeancesCsvStep")
                .<Seance, Seance>chunk(10)
                .reader(seanceCsvItemReader(null))
                .writer(seanceItemWriter(null))
                .faultTolerant()
                .skipPolicy(seanceSkipPolicy())
                .listener(chargementSeancesStepListener())
                .build();
    }

    @Bean
    public Step chargementSeancesTxtStep(final StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("chargementSeancesTxtStep")
                .<Seance, Seance>chunk(10)
                .reader(seanceTxtItemReader(null))
                .writer(seanceItemWriter(null))
                .faultTolerant()
                .skipPolicy(seanceSkipPolicy())
                .listener(chargementSeancesStepListener())
                .build();
    }

    @Bean
    public StepExecutionListener chargementSeancesStepListener() {
        return new ChargementSeancesStepListener();
    }
}
