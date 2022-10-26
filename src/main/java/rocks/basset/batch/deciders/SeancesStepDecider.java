package rocks.basset.batch.deciders;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.util.StringUtils;

public class SeancesStepDecider implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if(StringUtils.endsWithIgnoreCase(stepExecution.getJobParameters().getString("seancesFile"), "csv")){
            return new FlowExecutionStatus("csv");
        }

        return new FlowExecutionStatus("txt");
    }
}
