package factory.json;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class JsonBeanDefinitionDocumentReader  {


    public void registerBeanDefinitions(JsonBeans beans, BeanDefinitionRegistry registry) {

        for (JsonBean jsonBean : beans.beanlist) {
            AbstractBeanDefinition abstractBeanDefinition = null;
            try {
                abstractBeanDefinition = BeanDefinitionReaderUtils.createBeanDefinition(
                        null, jsonBean.getClazz(), this.getClass().getClassLoader());
                abstractBeanDefinition.getConstructorArgumentValues().addGenericArgumentValue(jsonBean.getValue());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            registry.registerBeanDefinition(jsonBean.getName(),abstractBeanDefinition);
        }
    }
}
