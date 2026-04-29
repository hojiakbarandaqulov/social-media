package org.example.service;

import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CreateCommentRequest;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(Long currentUserId, Long postId, CreateCommentRequest request);

    List<CommentResponse> getCommentsByPostId(Long postId);
}
