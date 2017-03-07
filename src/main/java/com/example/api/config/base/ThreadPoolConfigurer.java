package com.example.api.config.base;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Bean definitions for thread pools.
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@Configuration
@EnableAsync
@EnableScheduling
public class ThreadPoolConfigurer {


}
