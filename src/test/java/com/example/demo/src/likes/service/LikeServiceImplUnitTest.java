package com.example.demo.src.likes.service;

import com.example.demo.src.likes.domain.Like;
import com.example.demo.src.likes.repository.LikeRepository;
import com.example.demo.src.member.domain.Member;
import com.example.demo.src.member.repository.MemberRepository;
import com.example.demo.src.product.domain.Product;
import com.example.demo.src.product.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


/**
 * @SpringBootTest는 테스트를 위한 스프링 어플리케이션 컨텍스트를 만드는데 사용한다.
 * LikeService는 빈으로 등록되어 스프링 컨텍스트에서 테스트 클래스에 의존성으로 주입된다.
 * <p>
 * ExtendWith으로 모키토에 모의로 종속성이 설정된다.
 * InjestMocks로 모의 인스턴스로 생성
 */
@ExtendWith(MockitoExtension.class)
public class LikeServiceImplUnitTest {
    @InjectMocks
    private LikeServiceImpl likeServiceImpl;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private MemberRepository memberRepository;

    @Nested
    class LikedUnitTests {
        @Test
        @DisplayName("좋아요 Mock 유닛테스트(성공)")
        void addLikesCount() {
            Long mockProductIdx = 1L;
            String mockEmail = "kdh3213@gmail.com";
            Member member = mock(Member.class);
            Product product = mock(Product.class); // Mock 상품 객체 생성하고

            given(memberRepository.findMemberByUsername(eq(mockEmail))).willReturn(member);
            given(productRepository.findById(eq(mockProductIdx))).willReturn(Optional.of(product)); // Mock 상품 Idx를 찾아서 product 반환 상황 부여
            given(likeRepository.existsByProductAndMember(eq(product), eq(member))) // 좋아요 기록은 없음으로 부여하고
                    .willReturn(false);

            likeServiceImpl.addLikesCount(mockProductIdx, mockEmail); // 좋아요 증가 수행 후에

            verify(productRepository, times(1)).findById(eq(mockProductIdx)); //검증
            verify(likeRepository, times(1)).existsByProductAndMember(eq(product), eq(member)); ///검증
            verify(likeRepository, times(1)).save(any(Like.class));
            verify(product, times(1)).increaseLikesCount(); //최종적으로 좋아요 증가 됐는지 검증
        }


    }

}