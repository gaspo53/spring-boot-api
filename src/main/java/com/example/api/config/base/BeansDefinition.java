package com.example.api.config.base;

import java.util.Properties;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

/**
 * Bean definitions for the application.<br>
 * Here are defined beans that require initialisation.
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
@Configuration
public class BeansDefinition {
	
	@Autowired
	private Environment env;
    
	@Autowired
	private ApplicationContext applicationContext;
	
    @Bean(name="javaMailSender")
    public JavaMailSenderImpl javaMailSenderImplConfigurer(){
    	JavaMailSenderImpl bean = new JavaMailSenderImpl();
    	
    	String host = this.getEnv().getProperty("mail.host");
    	String username = this.getEnv().getProperty("mail.username");
    	String password = this.getEnv().getProperty("mail.password");
    	Properties properties = new Properties();
    	
    	properties.setProperty("mail.smtp.auth", "true");
    	properties.setProperty("mail.smtp.starttls.enable", "true");
    	
    	bean.setHost(host);
    	bean.setUsername(username);
    	bean.setPassword(password);
    	bean.setJavaMailProperties(properties);
    	
    	return bean;
    }

    @Bean(name="templateMessage")
    public SimpleMailMessage simpleMailMessageConfigurer(){
    	SimpleMailMessage bean = new SimpleMailMessage();
    	String from = this.getEnv().getProperty("mail.from");
    	bean.setFrom(from);
    	
    	return bean;
    }
    
    
    /**
     * Creates an instance de {@link CustomHttpClient}
     * @return a {@link HttpClient}
       @author gaspar
     */
    @Bean
    public CloseableHttpClient createHttpClient() {
    	
    	PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
    	
    	connectionManager.setDefaultMaxPerRoute(50);
    	HttpHost localhost = new HttpHost("localhost", 80);
    	connectionManager.setMaxPerRoute(new HttpRoute(localhost), 50);    	
    	
    	CloseableHttpClient client = HttpClients.custom()
    										 .setConnectionManager(connectionManager)
    										 .build();
        return client;
    }
  
    
    //Getters and setters 
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
