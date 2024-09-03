package com.wodnj5.board.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.wodnj5.board.domain.entity.PostEntity;
import com.wodnj5.board.domain.entity.QPostEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomPostRepositoryImpl implements CustomPostRepository {

    @PersistenceContext
    private final EntityManager em;

    @Override
    public Page<PostEntity> search(String search, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QPostEntity p = new QPostEntity("p");

        List<PostEntity> results = queryFactory
                .selectFrom(p)
                .where(p.title.contains(search).or(p.contents.contains(search)))
                .orderBy(p.createdDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(results, pageable, results.size());
    }
}
