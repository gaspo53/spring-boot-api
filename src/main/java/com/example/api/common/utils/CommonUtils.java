package com.example.api.common.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.example.api.common.filter.Page;
import com.example.api.common.filter.Result;
/**
 * This class has a set of utils, mostly for property handling.<br>
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@Component("commonUtils")
public class CommonUtils {

	@Autowired
	private Environment env;

	@Autowired
	private MessageSource messageSource;
	
	
	/**
	 * Get a message with the key received, in a locale context of the actual user.<br>
	 * Also, it can receive as many parameters as the user want.<br>
	 * 
	 * @param key (the placeholder)
	 * @param params (array of strings)
	 * @param locale
	 * @return the message in the current context locale
	 */
	public String getMessage(String key, Object[] params, Locale locale){
		String message = key;
		try{
			message = this.getMessageSource().getMessage(key, params, locale);
		}catch(Exception e){
			LogHelper.error(this,e);
		}
		return message;		
	}

	/**
	 * Get a message with the key received, in the default locale context<br>
	 * Also, it can receive as many parameters as the user want.<br>
	 * 
	 * @param key (the placeholder)
	 * @param params (array of strings)
	 * @param locale
	 * @return the message in the actual context locale
	 */
	public String getMessage(String key, Object[] params){
		String message = this.getMessage(key, params, LocaleContextHolder.getLocale());
		return message;		
	}
	
	/**
	 * Same as {@link #getMessage(String, Object[], Locale)}, but with no params
	 * @param key
	 * @param locale
	 * @return the message in the actual context locale
	 */
	public String getMessage(String key, Locale locale){
		return this.getMessage(key, new Object[]{}, locale);		
	}
	
	/**
	 * Same as {@link #getMessage(String, Locale)}, but with no Locale
	 * @param key
	 * @param locale
	 * @return the message in the actual context locale
	 */
	public String getMessage(String key){
		return this.getMessage(key, LocaleContextHolder.getLocale());		
	}
	
	/**
	 * Resolves the placeholder received in key, but only in the application.properties file.<br>
	 * @param key
	 * @return the value as string
	 */
	public String getProperty(String key) {
		return this.getEnv().getProperty(key);
	}

	/**
	 * Resolves the placeholder received in key, but only in the application.properties file.<br>
	 * Casts to the received type.
	 * @param key
	 * @return the value as string
	 */
	public <E> E getProperty(String key, Class<E> type) {
		return this.getEnv().getProperty(key,type);
	}


	/**
     * Paginates a Result, according to the Page passed.<br>
     * Useful to do pagination when consuming WebServices, or Lists.
     * 
     * @param list
     * @param page
     * @see {@link Result}
     * @return
     */
    
    @SuppressWarnings("unchecked")
	public <E> Result<E> getResult(List<E> list, Page page){
    	Result<E> result = new Result<E>();
    	result.setPage(page);
    	result.setTotalResults(CollectionUtils.size(list));
    	int from = 0;
    	
    	if (page.getPageNumber() <= 1){
    		from = 0;
    	}else{
    		from = (page.getPageNumber()-1) * page.getPageSize();
    	}

    	int to = from + page.getPageSize();
    	Object[] array = list.toArray();
    	Object[] subarray = ArrayUtils.subarray(array, from, to);
    	
		List<E> subList = new ArrayList<E>();
		subList.addAll((Collection<? extends E>) Arrays.asList(subarray));
    	
    	result.setResult(subList);
    	
    	return result;
    }

    //Getters and setters
	public Environment getEnv() {
		return this.env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}


	public MessageSource getMessageSource() {
		return this.messageSource;
	}


	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
