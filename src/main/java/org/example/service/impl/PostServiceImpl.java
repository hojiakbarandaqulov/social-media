package org.example.service.impl;

import org.example.common.exception.ResourceNotFoundException;
import org.example.dto.post.CreatePostRequest;
import org.example.dto.post.PostResponse;
import org.example.entity.Post;
import org.example.entity.User;
import org.example.mapper.PostMapper;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.example.service.PostService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postMapper = postMapper;
    }

    @Override
    @Transactional
    public PostResponse createPost(Long currentUserId, CreatePostRequest request) {
        User author = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("Foydalanuvchi topilmadi."));

        Post post = new Post();
        post.setContent(request.content().trim());
        post.setAuthor(author);

        return postMapper.toResponse(postRepository.save(post));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PostResponse> getFeed() {
        return postRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(postMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post topilmadi."));
        return postMapper.toResponse(post);
    }
}
