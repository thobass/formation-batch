package rocks.basset.batch;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import rocks.basset.batch.dao.FormateurDAO;
import rocks.basset.batch.services.MailContentGenerator;
import rocks.basset.batch.services.PlanningMailSenderService;

import static org.junit.jupiter.api.Assertions.*;


class ChargementFormateursStepConfigTest extends BaseTest{

    @Autowired
    private FormateurDAO formateurDAO;

    @Test
    void shouldLoadFormateursWithSuccess(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("chargementFormateursStep", jobParameters);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        Integer count = formateurDAO.count();
        assertEquals(16, count);
    }
}
