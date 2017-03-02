package com.puresoltechnologies.lifeassist.common.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerializer {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toString(Object o) throws JsonProcessingException {
	return mapper.writeValueAsString(o);
    }

    public static <T> T fromString(String serialized, Class<T> type) throws IOException {
	return mapper.readValue(serialized, type);
    }

}
