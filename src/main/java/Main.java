import factory.JsonBeanFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.io.ClassPathResource;

public class Main {

    public static void main(String[] args) {

        BeanFactory beanFactory = new JsonBeanFactory(new ClassPathResource("haha.json"));

        String result = beanFactory.getBean(String.class);
        System.out.println(result);
    }
}
