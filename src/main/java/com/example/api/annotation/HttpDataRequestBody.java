package com.example.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.google.common.base.CaseFormat;

/**
 * Indicates that the entity has to be converted to JSON, and to be sent in the Request Body.
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpDataRequestBody {
    /**
     * Whether the JSON serialization has to be converted to key : value or not. Default is false.
     * @return
     */
    boolean asProperty() default false;
    
    /**
     * The format to the parameter to be converted into.
     * @return
     */
    CaseFormat format() default CaseFormat.LOWER_CAMEL;
    
}
