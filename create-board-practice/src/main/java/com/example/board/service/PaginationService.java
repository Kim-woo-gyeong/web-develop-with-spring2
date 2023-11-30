package com.example.board.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PaginationService {
    // 페이지네이션 바의 길이를 정해야함. -> 상태값으로 존재하게 하여 쉽게하자.
    private static final int BAR_LENGTH = 5;
    public List<Integer> getPaginationBarNumbers(int currentPageNumber, int totalPage) {
        // ex) 0 1 2 3 4
        int startNumber = Math.max(currentPageNumber - BAR_LENGTH / 2, 0);
        int endNumber = Math.min(startNumber + BAR_LENGTH, totalPage);

        // startNumber 이상 endNumber 미만 으로 범위 생성.
        return IntStream.range(startNumber, endNumber).boxed().toList();
    }

    // 현재 이 서비스가 알고 있는 bar length 를 조회할 수 있게끔, getter 역할용.
    public int currentBarLength(){
        return BAR_LENGTH;
    }
}
