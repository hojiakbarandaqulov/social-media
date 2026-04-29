package org.example.service.impl;

import org.example.common.exception.ResourceNotFoundException;
import org.example.dto.comment.CommentResponse;
import org.example.dto.comment.CreateCommentRequest;
import org.example.entity.Comment;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.mapper.CommentMapper;
import org.example.repository.CommentRepository;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.service.CommentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository,
            CommentMapper commentMapper
    ) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    @Transactional
    public CommentResponse createComment(Long currentUserId, Long postId, CreateCommentRequest request) {
        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post topilmadi."));

        Comment comment = new Comment();
        comment.setContent(request.content().trim());
        comment.setAuthor(author);
        comment.setPost(post);

        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsByPostId(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post topilmadi.");
        }

        return commentRepository.findAllByPostIdOrderByCreatedAtAsc(postId).stream()
                .map(commentMapper::toResponse)
                .toList();
    }
}
