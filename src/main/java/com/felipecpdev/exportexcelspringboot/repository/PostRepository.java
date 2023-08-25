package com.felipecpdev.exportexcelspringboot.repository;

import com.felipecpdev.exportexcelspringboot.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
