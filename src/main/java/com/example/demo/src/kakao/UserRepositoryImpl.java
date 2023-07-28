package com.example.demo.src.kakao;

import com.example.demo.src.auth.domain.Members;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

   // private final JPAQueryFactory jpaQueryFactory;

    /*public Members findById(Long userId) {
        QMembers qMembers = QMembers.members;
        BooleanBuilder condition = new BooleanBuilder();
        condition.and(qMembers.memberIdx.eq(userId));
        return  queryFactory.selectFrom(qMembers)
                .where(condition)
                .fetchOne();
    }*/
}
