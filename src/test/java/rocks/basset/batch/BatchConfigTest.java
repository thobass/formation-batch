package rocks.basset.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import rocks.basset.batch.dao.FormateurDAO;
import rocks.basset.batch.dao.FormationDAO;
import rocks.basset.batch.dao.SeanceDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BatchConfigTest extends BaseTest{

    @Autowired
    FormationDAO formationDAO;

    @Autowired
    private FormateurDAO formateurDAO;

    @Autowired
    private SeanceDAO seanceDAO;

    @Test
    void shouldExecuteJobWithSuccess() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("formateursFile", "classpath:inputs/formateursFile.csv")
                .addString("formationsFile", "classpath:inputs/formationsFile.xml")
                .addString("seancesFile", "classpath:inputs/seancesFile.csv")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        assertEquals(formateurDAO.count(), 16);
        assertEquals(formationDAO.count(), 4);
        assertEquals(seanceDAO.count(), 19);
        assertEquals(greenMail.getReceivedMessages().length, 4);
    }
}
