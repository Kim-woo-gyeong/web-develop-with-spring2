package com.example.board.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
// 대표적인 옵션 WebEnvironment : NONE 을 하면 아무것도 안 넣겠다 -> springboot 무게를 줄일 수 있음.
// classes : 설정 클래스를 지정할 수 있음. 모든 bean 스캔 대상들을 Configuration 으로 불러들임. void 를 하면 아무것도 아닌게 됨.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
public class PaginationServiceTest {

    private final PaginationService svc;
    public PaginationServiceTest(@Autowired PaginationService svc){
        this.svc = svc;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면, 페이징 바 리스트를 만들어준다.")
    // junitfy 테스트 기법인 parameter test 적용.
    @MethodSource
    @ParameterizedTest(name = "[{index}] {0}, {1} => {2}")
    void givenCurrentPageAndTotalPage_whenGetPaginationBarNumbers_thenReturnBarNumbers(
            int currentPageNumber, int totalPageNumber, List<Integer> expected
    ){
        // given

        // when
        List<Integer> actual = svc.getPaginationBarNumbers(currentPageNumber, totalPageNumber);
        //then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageAndTotalPage_whenGetPaginationBarNumbers_thenReturnBarNumbers(){
        return Stream.of(
                arguments(0, 13, List.of(0,1,2,3,4)),
                arguments(1, 13, List.of(0,1,2,3,4)),
                arguments(2, 13, List.of(0,1,2,3,4)),
                arguments(3, 13, List.of(1,2,3,4,5)),
                arguments(4, 13, List.of(2,3,4,5,6)),
                arguments(5, 13, List.of(3,4,5,6,7)),
                arguments(6, 13, List.of(4,5,6,7,8)),
                arguments(10, 13, List.of(8,9,10,11,12)),
                arguments(11, 13, List.of(9,10,11,12)),
                arguments(12, 13, List.of(10,11,12))
        );
    }

    // 페이지네이션의 길이가 5임을 드러내고 싶어서.
    @DisplayName("현재 설정되어 있는 페이지네이션 바의 길이를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnCurrentBarLength(){
        // Given
        // When
        int barLength = svc.currentBarLength();
        // Then
        assertThat(barLength).isEqualTo(5);
    }
}
