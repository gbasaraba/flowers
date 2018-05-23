package org.basaraba.flowers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Main app configuration.
 */
@Component
@ConfigurationProperties
public class ApplicationConfig {

    private int localServerPort;
    private String postsBaseUri;
    private String remotePostRepositoryEndpoint;
    private String localHostName;

    public int getLocalServerPort() {
        return localServerPort;
    }

    @Value("${servers.local.port}")
    public void setLocalServerPort(int localServerPort) {
        this.localServerPort = localServerPort;
    }

    public String getPostsBaseUri() {
        return postsBaseUri;
    }

    @Value("${servers.local.baseUri}")
    public void setPostsBaseUri(String postsBaseUri) {
        this.postsBaseUri = postsBaseUri;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer = new PropertySourcesPlaceholderConfigurer();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        yaml.setResources(new ClassPathResource("application.yml"));
        propertySourcesPlaceholderConfigurer.setProperties(yaml.getObject());
        return propertySourcesPlaceholderConfigurer;
    }

    public String getRemotePostRepositoryEndpoint() {
        return remotePostRepositoryEndpoint;
    }

    @Value("${servers.remote.postRepositoryEndpoint}")
    public void setRemotePostRepositoryEndpoint(String remotePostRepositoryEndpoint) {
        this.remotePostRepositoryEndpoint = remotePostRepositoryEndpoint;
    }

    public String getLocalHostName() {
        return localHostName;
    }

    @Value("${servers.local.hostName}")
    public void setLocalHostName(String localHostName) {
        this.localHostName = localHostName;
    }
}