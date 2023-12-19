package com.example.board.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/*
* 테스트 목적으로 구현,
* 인코드를 했을 때 결과가 어떤 문자열이 나오는지 테스트 하기 위함.
* ( param1&&param2.. 쿼리 파라미터의 형태임.)
* */
@TestComponent
public class FormDataEncoder {
    private final ObjectMapper mapper;

    public FormDataEncoder(ObjectMapper mapper){
        this.mapper = mapper;
    }

    public String encode(Object obj) {
        // obj -> MultiValueMap 을 통해 쿼리 파라미터 부분만 추출됨.
        Map<String, String> fieldMap = mapper.convertValue(obj, new TypeReference<>() {});
        MultiValueMap<String, String> valueMap = new LinkedMultiValueMap<>();
        valueMap.setAll(fieldMap);

        return UriComponentsBuilder.newInstance()
                .queryParams(valueMap)
                .encode()
                .build()
                .getQuery();
    }
}
