package org.basaraba.flowers.posts;

import org.basaraba.flowers.posts.data.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// a toy forwarding implementation, fetches from the remote
class PostRepositoryRemoteImpl implements PostRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostRepositoryRemoteImpl.class);
    private final String endpoint;
    private final String uri;
    PostRepositoryRemoteImpl(String endpoint, String uri) {
        this.endpoint = endpoint;
        this.uri = uri;
    }

    PostRepository init() {
        return this;
    }

    @Override
    public Mono<Post> postForId(int id) {
       var next = this.fetchFromRemote()
               .filter(post -> post.postId() == id).next();
       return next;
    }

    @Override
    public Flux<Post> allPosts() {
        return this.fetchFromRemote();
    }

    @Override
    public Mono<Void> savePost(Mono<Post> post) {
        return Mono.empty();
    }

    private Flux<Post> fetchFromRemote() {
        WebClient client = WebClient.create("http://" + this.endpoint);
        return client.get().uri("/posts").exchange()
                .flatMapMany(response -> response.bodyToFlux(Post.class));
    }
}
