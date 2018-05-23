package org.basaraba.flowers.posts;

import org.basaraba.flowers.posts.data.Post;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

class PostHandler {
    private final PostRepository repository;

    PostHandler(PostRepository repository) {
        this.repository = repository;
    }

    // TODO(GregB) make all of these configurable
    RouterFunction<?> route() {
        return RouterFunctions
                    .route(GET("/posts/uniqueUserIdCount").and(accept(APPLICATION_JSON)), this::uniqueIdCount)
                    .andRoute(GET("/posts/{id}").and(accept(APPLICATION_JSON)), this::getPost)
                    .andRoute(PUT("/posts/{id}").and(accept(APPLICATION_JSON)), this::savePost)
                    .andRoute(GET("/posts").and(accept(APPLICATION_JSON)), this::allPosts)
                    .andRoute(POST("/posts").and(contentType(APPLICATION_JSON)), this::create);
    }

    private Mono<ServerResponse> savePost(ServerRequest request) {
        Mono<Post> post = request.bodyToMono(Post.class);
        this.repository.savePost(post);
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(post, Post.class);
    }

    private Mono<ServerResponse> create(ServerRequest request) {
        Mono<Post> post = request.bodyToMono(Post.class);
        return ServerResponse.ok().build(this.repository.savePost(post));
    }

    private Mono<ServerResponse> allPosts(ServerRequest request) {
       Flux<Post> posts = this.repository.allPosts();
       return ServerResponse.ok().contentType(APPLICATION_JSON).body(posts, Post.class);
    }

    private Mono<ServerResponse> uniqueIdCount(ServerRequest request) {
        Mono<Long> posts = this.repository.allPosts().map(post -> post.userId()).distinct().count();
        return ServerResponse.ok().contentType(APPLICATION_JSON).body(posts, Long.class);
    }

   private Mono<ServerResponse> getPost(ServerRequest request) {
        int postId = Integer.valueOf(request.pathVariable("id"));
        Flux<Post> posts = this.repository.allPosts().filter(post -> post.postId() == postId);
//       Flux<Post> posts = this.repository.allPosts().filter( post -> post.postId() == postId);
       return ServerResponse.ok().contentType(APPLICATION_JSON).body(posts, Post.class);
//
//        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
//      7  Mono<Post> postMono = this.repository.postForId(postId);
//        return postMono
//                .flatMap(post -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(post)))
//                .switchIfEmpty(notFound);
    }
}
