package com.wodnj5.board.repository.post;

import static com.wodnj5.board.domain.QPostEntity.postEntity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wodnj5.board.domain.PostEntity;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    @Autowired
    private final EntityManager em;

    @Override
    public Page<PostEntity> search(String search, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있다면 조건 추가
        if (search != null) {
            builder.and(postEntity.title.contains(search));
        }

        // 전체 게시글 개수 조회
        Long total = queryFactory.select(postEntity.count())
                .from(postEntity)
                .where(builder)
                .fetchOne();

        // 페이징 쿼리
        List<PostEntity> result = queryFactory
                .selectFrom(postEntity)
                .where(builder)
                .orderBy(postEntity.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(result, pageable, total);
    }
}
