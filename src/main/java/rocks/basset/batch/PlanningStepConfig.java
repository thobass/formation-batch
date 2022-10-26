package rocks.basset.batch;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import rocks.basset.batch.domain.Planning;
import rocks.basset.batch.mappers.PlanningRowMapper;
import rocks.basset.batch.processors.PlanningProcessor;
import rocks.basset.batch.services.MailContentGenerator;
import rocks.basset.batch.services.MailContentGeneratorImpl;
import rocks.basset.batch.services.PlanningMailSenderService;
import rocks.basset.batch.services.PlanningMailSenderServiceImpl;
import rocks.basset.batch.writers.PlanningItemWriter;

import javax.sql.DataSource;
import java.io.IOException;

@Configuration
public class PlanningStepConfig {

    @Bean(destroyMethod = "")
    public JdbcCursorItemReader<Planning> planningItemReader(final DataSource datasource){
        return new JdbcCursorItemReaderBuilder<Planning>()
                .name("planningItemReader")
                .dataSource(datasource)
                .sql("select distinct f.* from formateurs f join seances s on f.id=s.id_formateur")
                .rowMapper(new PlanningRowMapper())
                .build();
    }

    @Bean
    public ItemProcessor<Planning, Planning> planningProcessor(final NamedParameterJdbcTemplate jdbcTemplate){
        return new PlanningProcessor(jdbcTemplate);
    }

    @Bean
    public PlanningItemWriter planningWriter(final PlanningMailSenderService planningService
            , final MailContentGenerator mailContentGenerator) {
        return new PlanningItemWriter(planningService, mailContentGenerator);
    }

    @Bean
    public MailContentGenerator mailContentGenerator(final freemarker.template.Configuration conf) throws IOException {
        return new MailContentGeneratorImpl(conf);
    }

    @Bean
    public PlanningMailSenderService planningMailSenderService(final JavaMailSender javaMailSender){
        return new PlanningMailSenderServiceImpl(javaMailSender);
    }

    @Bean
    public Step planningStep(StepBuilderFactory stepBuilderFactory){
        return stepBuilderFactory.get("planningStep")
                .<Planning, Planning>chunk(10)
                .reader(planningItemReader(null))
                .processor(planningProcessor(null))
                .writer(planningWriter(null, null))
                .build();
    }
}
