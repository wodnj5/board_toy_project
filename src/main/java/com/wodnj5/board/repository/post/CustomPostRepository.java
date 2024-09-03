package com.wodnj5.board.repository.post;

import com.wodnj5.board.domain.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomPostRepository {

    Page<PostEntity> search(String keyword, Pageable pageable);
}
