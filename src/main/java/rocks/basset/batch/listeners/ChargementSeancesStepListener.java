package rocks.basset.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepListenerSupport;
import rocks.basset.batch.domain.Formateur;
import rocks.basset.batch.domain.Seance;

@Slf4j
public class ChargementSeancesStepListener extends StepListenerSupport<Seance, Seance> implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Chargement des seances : {} seance(s) enregistré(s)", stepExecution.getReadCount());
        return stepExecution.getExitStatus();
    }
}
