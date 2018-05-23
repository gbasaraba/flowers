package org.basaraba.flowers.posts.functional;

import org.awaitility.Awaitility;
import org.basaraba.flowers.events.ContextInitialized;
import org.basaraba.flowers.posts.data.Post;
import org.basaraba.utils.TestEventListener;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseFunctionalTest {
    private static FlowersAppBootstrap app;
    private TestEventListener eventListener;

    @Test
    void testGetAll() {
        long count = WebClient.create(app.getEndpoint())
                .get().uri("/posts").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(r -> r.bodyToFlux(Post.class)).count().block();
        assertEquals(100, count);
    }

    @Test
    void testGetById() {
        var post = blockingPostById(4);
        assertEquals(new Post(1, 4, "roses", "are red"), post);
    }

    private static final Post POST_ID_4 = new Post(1, 4, "sunflower", "rose");

    @Test
    void savePost() {
        Post postId4 = blockingPostById(4);
        assertEquals(POST_ID_4, postId4);
        var updated = postId4.title("800FLOWERS").body("800FLOWERS");
        var client = WebClient.create(app.getEndpoint());
        Mono<Post> result = client.put().uri("/posts/{id}", updated.postId())
                .body(BodyInserters.fromObject(updated))
                .exchange().flatMap(clientResponse -> clientResponse.bodyToMono(Post.class));

        var response = result.block();
        assertTrue(response.postId() == 4 &&
                response.body().equals("800FLOWERS") &&
                response.title().equals("800FLOWERS"));
    }

    @Test
    void testGetUniqueId() {
        long count = WebClient.create(app.getEndpoint())
                .get().uri("/posts/uniqueUserIdCount").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .map(r -> r.bodyToMono(Long.class)).block().block();
        assertEquals(10, count);
    }

    @Disabled("use to indefinitely run the servers... useful for connectign with other tools")
    @Test
    void run() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    @BeforeEach
    void setup() {
        if (isNull(app)) {
            app = new FlowersAppBootstrap().init();
            // wait for the spring context to be initialized
            eventListener = app.testEventListener();
            Awaitility.await().atMost(50, TimeUnit.SECONDS).until((() ->
                    eventListener.validateEventOccurs(ContextInitialized.class)));
        }
    }

    @AfterAll
    static void cleanup() {
        app.stop();
    }

    private static Post blockingPostById(int id) {
        return WebClient.create(app.getEndpoint())
                .get().uri("/posts/" + id).accept(MediaType.APPLICATION_JSON)
                .exchange()
                .map(r -> r.bodyToFlux(Post.class)).block().blockFirst();
    }
}