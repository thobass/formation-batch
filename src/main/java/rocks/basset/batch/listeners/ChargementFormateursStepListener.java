package rocks.basset.batch.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepListenerSupport;
import rocks.basset.batch.domain.Formateur;

@Slf4j
public class ChargementFormateursStepListener extends StepListenerSupport<Formateur, Formateur> implements StepExecutionListener {

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Chargement des formateurs : {} formateur(s) enregistré(s)", stepExecution.getReadCount());
        return stepExecution.getExitStatus();
    }
}
