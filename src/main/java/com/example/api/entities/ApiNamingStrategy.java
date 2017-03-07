/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.entities;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.LowerCaseWithUnderscoresStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.PascalCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.PropertyNamingStrategy.UpperCamelCaseStrategy;

/**
 * @author gaspar
 *
 */
public class ApiNamingStrategy{

    /**
     * See {@link LowerCaseWithUnderscoresStrategy} for details.
     */
    public static final PropertyNamingStrategy SNAKE_CASE = new SnakeCaseStrategy();

    /**
     * See {@link PascalCaseStrategy} for details.
     * 
     * @since 2.1
     */
    public static final PropertyNamingStrategy PASCAL_CASE_TO_CAMEL_CASE = new UpperCamelCaseStrategy();

    /**
     * See {@link LowerCaseStrategy} for details.
     * 
     * @since 2.4
     */
    public static final PropertyNamingStrategy LOWER_CASE = new LowerCaseStrategy();
    
    /**
     * inLowerCamelCaseStyle
     * 
     * @since 2.4
     */
    public static final PropertyNamingStrategy LOWER_CAMEL_CASE = null;
    
    
    
}
