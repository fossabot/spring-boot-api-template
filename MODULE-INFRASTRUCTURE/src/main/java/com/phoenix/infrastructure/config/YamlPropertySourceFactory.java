package com.phoenix.infrastructure.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.IOException;
import java.util.Properties;

/**
 * However, by default, @PropertySource doesn't load YAML files. This fact is explicitly mentioned in the official
 * documentation.
 *
 * So, if we want to use the @PropertySource annotation in our application, we need to stick with the standard
 * properties files
 *
 * As of Spring 4.3, @PropertySource comes with the factory attribute. We can make use of it to provide our custom
 * implementation of the PropertySourceFactory, which will handle the YAML file processing.
 *
 * As we can see, it's enough to implement a single createPropertySource method.
 *
 * In our custom implementation, first, we used the YamlPropertiesFactoryBean to convert the resources in YAML format to
 * the java.util.PropertiesLoader object.
 *
 * Then, we simply returned a new instance of the PropertiesPropertySource, which is a wrapper that allows Spring
 * to read the parsed properties.
 *
 * EXAMPLE:
 * @PropertySource(value="classpath:database.yaml",factory = YamlPropertySourceFactory.class)
 */
@Configuration
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource)
            throws IOException {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());

        Properties properties = factory.getObject();

        return new PropertiesPropertySource(encodedResource.getResource().getFilename(), properties);
    }
}
