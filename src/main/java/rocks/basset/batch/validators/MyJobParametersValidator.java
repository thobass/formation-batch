package rocks.basset.batch.validators;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class MyJobParametersValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters jobParameters) throws JobParametersInvalidException {
        if(!StringUtils.endsWithIgnoreCase(jobParameters.getString("formateursFile"), "csv")){
            throw new JobParametersInvalidException("Le fichier des formateurs doit être au format CSV.");
        }

        if(!StringUtils.endsWithIgnoreCase(jobParameters.getString("formationsFile"), "xml")){
            throw new JobParametersInvalidException("Le fichier des formations doit être au format XML.");
        }

        if(!StringUtils.endsWithIgnoreCase(jobParameters.getString("seancesFile"), "csv") &&
                !StringUtils.endsWithIgnoreCase(jobParameters.getString("seancesFile"), "txt")){
            throw new JobParametersInvalidException("Le fichier des scéances doit être au format CSV ou TXT.");
        }
    }
}
