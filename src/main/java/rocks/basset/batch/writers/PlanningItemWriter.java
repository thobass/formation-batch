package rocks.basset.batch.writers;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import rocks.basset.batch.domain.Planning;
import rocks.basset.batch.services.MailContentGenerator;
import rocks.basset.batch.services.PlanningMailSenderService;

import java.util.List;

@RequiredArgsConstructor
public class PlanningItemWriter implements ItemWriter<Planning> {

    private final PlanningMailSenderService planningService;

    private final MailContentGenerator mailContentGenerator;

    @Override
    public void write(List<? extends Planning> list) throws Exception {
        for(Planning planning : list){
            String content = mailContentGenerator.generate(planning);
            planningService.send(planning.getFormateur().getAdresseEmail(), content);
        }
    }
}
