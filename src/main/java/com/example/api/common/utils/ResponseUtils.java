package com.example.api.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * 
 * Util to handle response transformations
 * @author Gaspar Rajoy <a href="mailto:grajoy@despegar.com">grajoy@despegar.com</a>
 *
 */
public class ResponseUtils {

    private static final ExecutorService pool = Executors.newFixedThreadPool(10);

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static ObjectMapper getJsonMapper() {
        return mapper;
    }
    /**
     * Converts a string with words splitted by underscores ("_"), into a string without
     * it and with each first letter in uppercase, but not the first.
     * @param value
     * @return
     */
    public static String underscoreToCamelCase(String value) {
        String[] strings = StringUtils.split(value.toLowerCase(), "_");
        for (int i = 1; i < strings.length; i++) {
            strings[i] = StringUtils.capitalize(strings[i]);
        }
        return StringUtils.join(strings);
    }

    /**
     * Creates a JSON-compliant String.<br>
     * The naming conventions will be in the namingStrategy received.
     * @param object
     * @param camelCase
     * @return
     */
    public static synchronized String createJsonString(Object object, PropertyNamingStrategy namingStrategy) {
        String jsonObject = null;
        try {
        	getJsonMapper().setPropertyNamingStrategy(namingStrategy);
            if (object != null) {
                jsonObject = getJsonMapper().writeValueAsString(object);
            } else {
                jsonObject = getJsonMapper().writeValueAsString("");
            }
        } catch (Exception e) {
            LogHelper.error(ResponseUtils.class, e);
        }

        return jsonObject;
    }

    /**
     * Creates a JSON-compliant String with underscores.<br>
     * @param object
     * @param camelCase
     * @see {@link PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES}
     * @return
     */
    public static synchronized String createJsonString(Object object) {
        String jsonObject = ResponseUtils.createJsonString(object, PropertyNamingStrategy.SNAKE_CASE);
        return jsonObject;
    }
    
    public static synchronized <E> E parseData(String jsonString, PropertyNamingStrategy namingStrategy,  Class<E> type) {
        E response = null;
        try {
            if (StringUtils.isNotBlank(jsonString)) {
                ObjectMapper mapper = getJsonMapper();
                mapper.setPropertyNamingStrategy(namingStrategy);
                response = mapper.readValue(jsonString, type);
            }
        } catch (Exception e) {
            LogHelper.error(ResponseUtils.class, e);
        }

        return response;
    }

    public static String toString(HttpEntity entity) {
        String content = StringUtils.defaultString(null);
        try {
            content = EntityUtils.toString(entity);
        } catch (Exception e) {
            LogHelper.error(ResponseUtils.class, e);
        }
        return content;
    }

    public static <T> List<T> convertListElementsType(List<?> list, PropertyNamingStrategy namingStrategy, Class<T> type) {
        ConcurrentLinkedQueue<T> concurrentList = new ConcurrentLinkedQueue<T>();

        if (CollectionUtils.isNotEmpty(list)) {
            Future<Void> future = convertList(type, namingStrategy, concurrentList, list);
            try {
                future.get();
            } catch (Exception e) {
                LogHelper.error(ResponseUtils.class, e);
            }
        }

        List<T> finalList = new ArrayList<T>(concurrentList);

        return finalList;
    }

    /**
     * Converts a response that is directly an array ( [{}...] ) into a {@link ItemsContainer}.
     * @param <E>
     * @param plainResponse
     * @param type
     * @return
     */
    public static <E> List<E> parseIterableData(String plainResponse, PropertyNamingStrategy namingStrategy, Class<E> type) {

        List<?> parseData = parseData(plainResponse, namingStrategy, List.class);
        List<E> listElementsType = convertListElementsType(parseData, namingStrategy, type);

        return listElementsType;

    }

    public static <T> ResponseEntity<T> createDefaultSuccessResponse(T data) {
        ResponseEntity<T> apiResponse = ResponseEntity.status(HttpStatus.OK).body(data);

        return apiResponse;
    }

    public static <T> ResponseEntity<T> cloneResponse(ResponseEntity<?> responseEntity, T data) {
        ResponseEntity<T> apiResponse = ResponseEntity.status(responseEntity.getStatusCode()).headers(responseEntity.getHeaders()).body(data);

        return apiResponse;
    }

    public static <T> ResponseEntity<T> createDefaultErrorResponse(String message) {
    	ResponseEntity<T> apiResponse = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        
        return apiResponse;
    }


    // Private methods
    private static <T> Future<Void> convertList(final Class<T> type, final PropertyNamingStrategy namingStrategy, final ConcurrentLinkedQueue<T> convertedList, final List<?> list) {
        Future<Void> future = pool.submit(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (Object element : list) {
                    String jsonString = ResponseUtils.createJsonString(element, namingStrategy);
                    T parsedData = parseData(jsonString, namingStrategy, type);
                    convertedList.add(parsedData);
                }
                return null;
            }
        });
        return future;
    }

}
