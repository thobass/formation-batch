package rocks.basset.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FormationsBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(FormationsBatchApplication.class, args);
    }

}
