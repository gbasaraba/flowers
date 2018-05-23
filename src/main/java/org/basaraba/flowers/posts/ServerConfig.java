package org.basaraba.flowers.posts;

import org.basaraba.flowers.ApplicationConfig;
import org.basaraba.flowers.server.Server;
import org.basaraba.flowers.server.ServerNettyImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.io.IOException;

@Component
public class ServerConfig {

    @Bean
    public ApplicationConfig applicationConfig() {
        return new ApplicationConfig();
    }

    @Bean
    Server server(ApplicationConfig applicationConfig, ApplicationEventPublisher applicationEventPublisher) {
        PostRepository repository = new PostRepositoryRemoteImpl(applicationConfig.getRemotePostRepositoryEndpoint(), applicationConfig.getPostsBaseUri()).init();
        PostHandler handler = new PostHandler(repository);
        RouterFunction<?> route = handler.route();
        Server server = new ServerNettyImpl("flowers-app-server", route);
        server.start(applicationConfig.getLocalHostName(), applicationConfig.getLocalServerPort());
        return server;
    }
}