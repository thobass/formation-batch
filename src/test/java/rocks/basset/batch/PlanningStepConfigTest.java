package rocks.basset.batch;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.test.context.jdbc.Sql;

import javax.mail.MessagingException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;


class PlanningStepConfigTest extends BaseTest{

    @Test
    @Sql(scripts = {"classpath:init-all-tables.sql"})
    void shouldSendPlanningWithSuccess() throws MessagingException {

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("planningStep");

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(greenMail.getReceivedMessages().length, 4);
    }
}
