package com.example.api.config.base;

import static com.google.common.base.Predicates.not;
import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.builders.PathSelectors.regex;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.UiConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Configuration for Swaggler.
 * @see {@link <a href="http://springfox.github.io/springfox/docs/snapshot/">Springfox docs</a>}
 * @author gaspar
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

	@Autowired
	private TypeResolver typeResolver;

	@Bean
	public Docket apiDocketConfig() {
		Docket docket = new Docket(DocumentationType.SWAGGER_2);
		
		docket.select()
  			  .apis(RequestHandlerSelectors.any())
			  .paths((PathSelectors.any()))
			  .paths(not(regex("/error")))
			  .paths(not(regex("/health")))
			  .paths(not(regex("/env.*")))
			  .paths(not(regex("/metrics.*")))
			  .paths(not(regex("/autoconfig")))
			  .paths(not(regex("/configprops")))
			  .paths(not(regex("/dump")))
			  .paths(not(regex("/info")))
			  .paths(not(regex("/beans")))
			  .paths(not(regex("/mappings")))
			  .paths(not(regex("/trace")))
			  .build()
			  .pathMapping("/")
			  .directModelSubstitute(LocalDate.class, String.class).genericModelSubstitutes(ResponseEntity.class)
			  .alternateTypeRules(newRule(this.typeResolver.resolve(DeferredResult.class,
										  this.typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
										  this.typeResolver.resolve(WildcardType.class))
			  )
			  .useDefaultResponseMessages(false)
			  .globalResponseMessage(RequestMethod.GET,newArrayList(new ResponseMessageBuilder().code(500)
																							  	.message("500 message")
																	 							.responseModel(new ModelRef("Error")).build())
			  )
			  .apiInfo(this.apiInfo())
			  .securitySchemes(newArrayList(this.apiKey())).securityContexts(newArrayList(this.securityContext()));
		
		return docket;
	}
	
	private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo(
                "Example API",
                "Bla bla.",
                "1.0",
                "hello@example.com",
                new Contact("Example.com", "", ""),
                "API License URL", null
        );
        return apiInfo;
    }
	
	private ApiKey apiKey() {
		return new ApiKey("mykey", "api_key", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(this.defaultAuth()).forPaths(PathSelectors.regex("/anyPath.*"))
				.build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return newArrayList(new SecurityReference("mykey", authorizationScopes));
	}

	@Bean
	UiConfiguration uiConfig() {
		return new UiConfiguration("validatorUrl");
	}
}
