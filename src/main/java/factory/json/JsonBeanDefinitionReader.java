package factory.json;

import com.google.gson.Gson;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.support.AbstractBeanDefinitionReader;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.NamedThreadLocal;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class JsonBeanDefinitionReader extends AbstractBeanDefinitionReader {
    private final ThreadLocal<Set<Resource>> resourcesCurrentlyBeingLoaded =
            new NamedThreadLocal<>("JSON bean definition resources currently being loaded");

    public JsonBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
        Assert.notNull(resource, "Resource must not be null");

        Set<Resource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
        if (currentResources == null) {
            currentResources = new HashSet<>(4);
            this.resourcesCurrentlyBeingLoaded.set(currentResources);
        }
        if (!currentResources.add(resource)) {
            throw new BeanDefinitionStoreException(
                    "Detected cyclic loading of " + resource + " - check your import definitions!");
        }

        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();

            return doLoadBeanDefinitions(new BufferedInputStream(inputStream));
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException(
                    "IOException parsing XML document from " + resource, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            currentResources.remove(resource);
            if (currentResources.isEmpty()) {
                this.resourcesCurrentlyBeingLoaded.remove();
            }
        }
    }

    private int doLoadBeanDefinitions(BufferedInputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        bufferedReader.lines().forEach(sb::append);

        Gson gson = new Gson();
        JsonBeans beans = gson.fromJson(sb.toString(),JsonBeans.class);

        int count = registerBeanDefinitions(beans);

        return count;
    }

    private int registerBeanDefinitions(JsonBeans beans) {
        JsonBeanDefinitionDocumentReader documentReader = new JsonBeanDefinitionDocumentReader();
        int countBefore = getRegistry().getBeanDefinitionCount();
        documentReader.registerBeanDefinitions(beans,getRegistry());
        return getRegistry().getBeanDefinitionCount() - countBefore;
    }
}
