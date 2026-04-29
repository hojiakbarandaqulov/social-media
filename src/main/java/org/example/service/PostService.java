package org.example.service;

import org.example.dto.post.CreatePostRequest;
import org.example.dto.post.PostResponse;

import java.util.List;

public interface PostService {

    PostResponse createPost(Long currentUserId, CreatePostRequest request);

    List<PostResponse> getFeed();

    PostResponse getPostById(Long postId);
}
