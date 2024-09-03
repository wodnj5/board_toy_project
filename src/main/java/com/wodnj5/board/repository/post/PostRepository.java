package com.wodnj5.board.repository.post;

import com.wodnj5.board.domain.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long>, CustomPostRepository {

}
