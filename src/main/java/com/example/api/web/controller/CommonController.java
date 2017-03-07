package com.example.api.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;

import com.example.api.common.filter.Page;
import com.example.api.common.utils.CommonUtils;

/**
 * The controller that is cross to all controllers in the application.<br>
 * It has common variables/methods, such as the Page (for pagination), the view to return, etc.
 *
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 **/
@Controller
public class CommonController {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private CommonUtils commonUtils;
	
	private ModelAndView view;
	
	private Page page;
	
	/**
	 * Default constructor. Initializes some members
	 */
	public CommonController(){
		this.setView(new ModelAndView());
		this.page = new Page();
	}
	
	/**
	 * This method updates the PageNumber and PageSize from the private attribute Page.
	 * @return the page
	 */
	public Page getPage(String page){
		this.page.setPageNumber(Integer.valueOf(page));
		this.page.setPageSize(this.getPageSize());
		return this.page;
	}

	/**
	 * This method updates the current private attribute Page to a non-paginated list. (limit 0)
	 * @return the page without limits
	 */
	public Page getPage()
	{
		this.page.setPageNumber(1);
		this.page.setPageSize(0);
		return this.page;
	}
	
	/**
	 * Sets a session attribute for the session stored in the request
	 * @param name
	 * @param value
	 * @param request
	 */
	protected void setSessionAttribute(String name, Object value, HttpServletRequest request){
		request.getSession().setAttribute(name,value);
	}

	/**
	 * Removes a session attribute for the session stored in the request
	 * @param name
	 * @param value
	 * @param request
	 */
	protected void removeSessionAttribute(String name, HttpServletRequest request){
		request.getSession().removeAttribute(name);
	}
	
	/**
	 * Returns a session attribute for the session stored in the request
	 * @param name
	 * @param value
	 * @param request
	 */
	public Object getSessionAttribute(String name, HttpServletRequest request){
		return request.getSession().getAttribute(name);
	}
	
	/**
	 * Invalidates the session
	 * @return
	 */
	public void invalidateSession(HttpServletRequest request){
		request.getSession().invalidate();
	}
	
	public int getPageSize(){
		String page_size = this.getEnv().getProperty("app.controller.page_size");
		return Integer.valueOf(page_size);
	}
	
	/** 
	 * Checks if a request was by AJAX
	 * 
	 * @return
	 */
	public boolean isAjax(HttpServletRequest request) {
		boolean isAjax = false;
		String xRequestedWithHeader = request.getHeader("X-Requested-With");
		isAjax = StringUtils.contains(xRequestedWithHeader,"XMLHttpRequest");
		
		return isAjax;
	}
	
	
	//Getters & setters
	public LocaleResolver getLocaleResolver() {
		return this.localeResolver;
	}

	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}

	public ModelAndView getView() {
		return this.view;
	}

	public void setView(ModelAndView view) {
		this.view = view;
	}
	
	public void setView(String viewName) {
		this.setView(new ModelAndView(viewName));
	}

	public CommonUtils getCommonUtils() {
		return this.commonUtils;
	}

	public void setCommonUtils(CommonUtils commonUtils) {
		this.commonUtils = commonUtils;
	}

	public Environment getEnv() {
		return this.env;
	}

	public void setEnv(Environment env) {
		this.env = env;
	}
	
}
