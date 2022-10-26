package rocks.basset.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.validators.MyJobParametersValidator;

import java.util.Arrays;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public JobParametersValidator defaultJobParametersValidator(){
        var bean = new DefaultJobParametersValidator();
        bean.setRequiredKeys(new String[] {"formateursFile","formationsFile","seancesFile"});
        bean.setOptionalKeys(new String[] {"run.id"});
        return bean;
    }

    @Bean
    public MyJobParametersValidator myJobParametersValidator(){
        return new MyJobParametersValidator();
    }

    @Bean
    public JobParametersValidator compositeJobParametersValidator(){
        var bean = new CompositeJobParametersValidator();
        bean.setValidators(Arrays.asList(defaultJobParametersValidator(), myJobParametersValidator()));
        return bean;
    }

    @Bean
    public Job job(final JobBuilderFactory jobBuilderFactory,
                   final Step chargementFormateursStep,
                   final Step chargementFormationsStep,
                   final Step chargementSeancesStep){
        return jobBuilderFactory.get("formation-batch")
                .incrementer(new RunIdIncrementer())
                .start(chargementFormateursStep)
                .next(chargementFormationsStep)
                .next(chargementSeancesStep)
                .validator(compositeJobParametersValidator())
                .build();
    }
}
