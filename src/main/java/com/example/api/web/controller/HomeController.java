package com.example.api.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springfox.documentation.annotations.ApiIgnore;

/**
 * Home controller
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
@ApiIgnore
@RestController
public class HomeController extends CommonController{

	
	
	@RequestMapping(value={"","/"})
	public String helloWord(){
		return "Hello world from Example API";
	}
	

	
	
}
