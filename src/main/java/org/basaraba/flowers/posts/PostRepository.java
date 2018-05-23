package org.basaraba.flowers.posts;

import org.basaraba.flowers.posts.data.Post;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Get / save posts
 */
interface PostRepository {
    Mono<Post> postForId(int id);

    Flux<Post> allPosts();

    Mono<Void> savePost(Mono<Post> post);
}