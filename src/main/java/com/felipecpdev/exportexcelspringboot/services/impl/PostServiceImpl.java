package com.felipecpdev.exportexcelspringboot.services.impl;

import com.felipecpdev.exportexcelspringboot.entity.Post;
import com.felipecpdev.exportexcelspringboot.repository.PostRepository;
import com.felipecpdev.exportexcelspringboot.services.PostService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
