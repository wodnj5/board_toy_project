package com.wodnj5.board.repository;

import com.wodnj5.board.domain.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<PostEntity> search(String keyword, Pageable pageable);
}
