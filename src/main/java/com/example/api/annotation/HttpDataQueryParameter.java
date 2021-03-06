package com.example.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.util.StringUtils;

import com.google.common.base.CaseFormat;

/**
 * Indicates that the attribute has to be in a HTTP query string.
 *
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpDataQueryParameter {
    
    /**
     * The name of this parameter. If left unspecified,
     * the name of the param is the name of the annotated method/field. If specified, the method/field
     * name is ignored.
     */
    String name() default "";
    
    /**
     * Whether the name has to be converted to camelCase or to underscore_case. Default is false.
     * @deprecated
     * @return
     */
    boolean camel() default false;
    
    /**
     * Whether the object (a Collection or Array) must be converted to String using {@link StringUtils#collectionToCommaDelimitedString(java.util.Collection)}
     * @return
     */
    boolean commaDelimited() default false;
    
    /**
     * Whether the name of has to be left as the declared member or not. Default is false
     * @return
     */
    boolean asDeclared() default false;
    
    /**
     * The format to the parameter to be converted into.
     * @return
     */
    CaseFormat format() default CaseFormat.LOWER_CAMEL;
    
}
