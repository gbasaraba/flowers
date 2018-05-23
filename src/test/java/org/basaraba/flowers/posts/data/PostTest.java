package org.basaraba.flowers.posts.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PostTest {

    @Test
    void general() {
        var post = new Post(1, 2, "title", "body");
        assertEquals(1, post.userId() );
        assertEquals(2, post.postId() );
        assertEquals("title", post.title() );
        assertEquals("body", post.body() );
    }
    @Test
    void userIdAndIdFulfillsEqualContract() {
        assertEquals(new Post(1,2, "A", "B"),
                     new Post(1, 2, "XY", "ZZ"));
    }

    @Test
    void basicHashTest() {
        assertNotEquals(new Post(1,2, "A", "B").hashCode(),
                        new Post(99, 2, "XY", "ZZ").hashCode());
    }
}