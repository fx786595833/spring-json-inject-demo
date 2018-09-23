package factory;

import factory.json.JsonBeanDefinitionReader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.Resource;

public class JsonBeanFactory extends DefaultListableBeanFactory {

    private final JsonBeanDefinitionReader reader = new JsonBeanDefinitionReader(this);

    public JsonBeanFactory(Resource resource) throws BeansException {
        this(resource, null);
    }

    public JsonBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
        super(parentBeanFactory);
        this.reader.loadBeanDefinitions(resource);
    }
}
