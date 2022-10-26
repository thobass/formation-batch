package rocks.basset.batch.services;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import rocks.basset.batch.domain.Planning;

import java.io.IOException;
import java.io.StringWriter;

public class MailContentGeneratorImpl implements MailContentGenerator {
    private Template template;

    public MailContentGeneratorImpl(final Configuration conf) throws IOException {
        super();
        this.template = conf.getTemplate("planning.ftl");
    }

    @Override
    public String generate(Planning planning) throws TemplateException, IOException {
        StringWriter sw = new StringWriter();
        template.process(planning, sw);
        return sw.toString();
    }
}
