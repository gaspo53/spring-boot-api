package com.example.api.config.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.example.api.config.converter.JavaBeanSnakeYamlHttpMessageConverter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Annotated web-config.<br>
 * 
 * @author Gaspar Rajoy -
 *         <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private Environment env;

	@Bean
	public EmbeddedServletContainerCustomizer servletContainerCustomizer() {
	    return new EmbeddedServletContainerCustomizer() {
	        @Override
	        public void customize(ConfigurableEmbeddedServletContainer servletContainer) {
	            ((TomcatEmbeddedServletContainerFactory) servletContainer).addConnectorCustomizers(
	                    new TomcatConnectorCustomizer() {
	                        @Override
	                        public void customize(Connector connector) {
	                            AbstractHttp11Protocol<?> httpProtocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
	                            httpProtocol.setCompression("on");
	                            httpProtocol.setCompressionMinSize(256);
	                            String mimeTypes = httpProtocol.getCompressableMimeTypes()+",text/css,application/javascript";
	                            String mimeTypesWithJson = mimeTypes + "," + MediaType.APPLICATION_JSON_VALUE;
	                            httpProtocol.setCompressableMimeType(mimeTypesWithJson);
	                        }
	                    }
	            );
	        }
	    };
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer bean = new PropertySourcesPlaceholderConfigurer();
		return bean;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		MappingJackson2HttpMessageConverter jsonConverter = this.mappingJacksonHttpConverter();
		converters.add(jsonConverter);

		JavaBeanSnakeYamlHttpMessageConverter yamlHttpMessageConverter = this.yamlHttpMessageConverter();
		converters.add(yamlHttpMessageConverter);

		super.configureMessageConverters(converters);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("classpath:/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		// Simple strategy: only path extension is taken into account
		MediaType yamlMediaType = new MediaType("application", "yaml");

		Map<String, MediaType> mediaTypes = new HashMap<String, MediaType>();
		mediaTypes.put("yaml", yamlMediaType);
		mediaTypes.put("json", MediaType.APPLICATION_JSON);

		configurer.useJaf(false).favorParameter(true).defaultContentType(MediaType.APPLICATION_JSON)
				.mediaTypes(mediaTypes);
	}

	/*
	 * ************** BEAN DEFINITIONS *****************************
	 */

	@Bean
	public JavaBeanSnakeYamlHttpMessageConverter yamlHttpMessageConverter() {
		JavaBeanSnakeYamlHttpMessageConverter converter = new JavaBeanSnakeYamlHttpMessageConverter();

		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(new MediaType("application", "yaml"));
		converter.setSupportedMediaTypes(supportedMediaTypes);

		return converter;
	}

	@Bean
	public MappingJackson2HttpMessageConverter mappingJacksonHttpConverter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		ObjectMapper controllerMapper = this.objectMapper();

		converter.setObjectMapper(controllerMapper);
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.APPLICATION_JSON);
		converter.setSupportedMediaTypes(supportedMediaTypes);

		return converter;
	}

	@Bean
	public ObjectMapper objectMapper() {
		// Mapper used to consume the API
		final ObjectMapper controllerMapper = new ObjectMapper();
		controllerMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);
		controllerMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
		controllerMapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
		controllerMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		controllerMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		controllerMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		controllerMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
		controllerMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		controllerMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return controllerMapper;
	}

	@Bean
	public RestOperations restOperations() {
		RestTemplate rest = new RestTemplate();
		// this is crucial!
		rest.getMessageConverters().add(0, this.mappingJacksonHttpConverter());

		return rest;
	}

	@Bean(name = "localeResolver")
	public LocaleResolver sessionLocaleResolver() {
		SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
		sessionLocaleResolver.setDefaultLocale(null);
		return sessionLocaleResolver;
	}

	// Request UTF-8 encoding
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		registrationBean.setFilter(characterEncodingFilter);
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		registrationBean.setOrder(Integer.MIN_VALUE);
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}

	// Getters and Setters
	public Environment getEnv() {
		return this.env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}
}
