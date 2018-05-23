package org.basaraba.flowers.posts.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Objects;

public final class Post {
    private final int userId;
    private final int postId;
    private final String title;
    private final String body;

    @JsonCreator
    public Post(@JsonProperty("userId") int userId, @JsonProperty("id") int postId,
                @JsonProperty("title") String title,@JsonProperty("body") String body) {
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.body = body;
    }

    @JsonProperty
    public int userId() {
        return userId;
    }

    @JsonProperty
    public int postId() {
        return postId;
    }

    @JsonProperty
    public String title() {
        return title;
    }

    @JsonProperty
    public String body() {
        return body;
    }

    public Post body(String body) {
        return new Post(this.userId, this.postId, this.title, body);
    }

    public Post title(String title) {
        return new Post(this.userId, this.postId, title, this.body);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("userId", userId)
                .append("postId", postId)
                .append("title", title)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return userId == post.userId &&
                postId == post.postId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
