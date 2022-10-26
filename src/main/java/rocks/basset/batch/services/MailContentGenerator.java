package rocks.basset.batch.services;

import freemarker.template.TemplateException;
import rocks.basset.batch.domain.Planning;

import java.io.IOException;

public interface MailContentGenerator {
    String generate(Planning planning) throws TemplateException, IOException;
}
