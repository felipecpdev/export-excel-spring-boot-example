package com.felipecpdev.exportexcelspringboot.services;

import com.felipecpdev.exportexcelspringboot.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> findAll();
}
