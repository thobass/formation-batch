package rocks.basset.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import rocks.basset.batch.dao.SeanceDAO;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ChargementSeancesStepConfigTest extends BaseTest{

    @Autowired
    private SeanceDAO seanceDAO;

    @Test
    @Sql(scripts = {"classpath:init-formations-formateurs-tables.sql"})
    void shouldLoadSeancesCSVWithSuccess(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("seancesFile", "classpath:inputs/seancesFile.csv")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("chargementSeancesCsvStep", jobParameters);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        Integer count = seanceDAO.count();
        assertEquals(19, count);
    }

    @Test
    @Sql(scripts = {"classpath:init-formations-formateurs-tables.sql"})
    void shouldLoadSeancesTXTWithSuccess(){
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("seancesFile", "classpath:inputs/seancesFile.txt")
                .toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchStep("chargementSeancesTxtStep", jobParameters);

        assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
        Integer count = seanceDAO.count();
        assertEquals(20, count);
    }
}
