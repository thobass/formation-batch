package rocks.basset.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.job.DefaultJobParametersValidator;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import rocks.basset.batch.deciders.SeancesStepDecider;
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
    public JobExecutionDecider seancesStepDecider(){
        return new SeancesStepDecider();
    }

    @Bean
    public Flow chargementFormateurFlow(final Step chargementFormateursStep){
        return new FlowBuilder<Flow>("ChargementFormateursFlow")
                .start(chargementFormateursStep)
                .end();
    }

    @Bean
    public Flow chargementFormationsFlow(final Step chargementFormationsStep){
        return new FlowBuilder<Flow>("ChargementFormationsFlow")
                .start(chargementFormationsStep)
                .end();
    }

    @Bean
    public Flow parallelFlow(){
        return new FlowBuilder<Flow>("parallelFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(chargementFormateurFlow(null), chargementFormationsFlow(null))
                .end();
    }

    @Bean
    public Job job(final JobBuilderFactory jobBuilderFactory,
                   final Flow parallelFlow,
                   final Step chargementSeancesTxtStep,
                   final Step chargementSeancesCsvStep,
                   final Step planningStep){
        return jobBuilderFactory.get("formation-batch")
                .incrementer(new RunIdIncrementer())
                .start(parallelFlow)
                .next(seancesStepDecider()).on("txt").to(chargementSeancesTxtStep)
                .from(seancesStepDecider()).on("csv").to(chargementSeancesCsvStep)
                .from(chargementSeancesTxtStep).on("*").to(planningStep)
                .from(chargementSeancesCsvStep).on("*").to(planningStep)
                .end()
                .validator(compositeJobParametersValidator())
                .build();
    }
}
