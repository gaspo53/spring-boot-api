/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.entities.filter;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.example.api.common.filter.Filter;

/**
 * @author gaspar
 *
 */
public class RouteFilter implements Filter{

	private String origin;
	
	private String originCountry;
	
	private String originContinent;

	private String destination;
	
	private String destinationCountry;

	private String destinationContinent;
	
	private Long distance;

	
	@Override
	public void fillQuery(Query query) {
		
		if (this.getOrigin() != null){
			query.addCriteria(Criteria.where("origin").is(this.getOrigin()));
		}
		
		if (this.getOriginCountry() != null){
			query.addCriteria(Criteria.where("originCountry").is(this.getOriginCountry()));
		}
		
		if (this.getOriginContinent() != null){
			query.addCriteria(Criteria.where("originContinent").is(this.getOriginContinent()));
		}
		
		if (this.getDestination() != null){
			query.addCriteria(Criteria.where("destination").is(this.getDestination()));
		}
		
		if (this.getDestinationCountry() != null){
			query.addCriteria(Criteria.where("destinationCountry").is(this.getDestinationCountry()));
		}
		
		if (this.getDestinationContinent() != null){
			query.addCriteria(Criteria.where("destinationContinent").is(this.getDestinationContinent()));
		}
		
		if (this.getDistance() != null){
			query.addCriteria(Criteria.where("distance").is(this.getDistance()));
		}
		
	}

	
	//Getters and setters
	public String getOrigin() {
		return this.origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOriginCountry() {
		return this.originCountry;
	}

	public void setOriginCountry(String originCountry) {
		this.originCountry = originCountry;
	}

	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDestinationCountry() {
		return this.destinationCountry;
	}

	public void setDestinationCountry(String destinationCountry) {
		this.destinationCountry = destinationCountry;
	}

	public Long getDistance() {
		return this.distance;
	}

	public void setDistance(Long distance) {
		this.distance = distance;
	}


	public String getOriginContinent() {
		return this.originContinent;
	}


	public void setOriginContinent(String originContinent) {
		this.originContinent = originContinent;
	}


	public String getDestinationContinent() {
		return this.destinationContinent;
	}


	public void setDestinationContinent(String destinationContinent) {
		this.destinationContinent = destinationContinent;
	}
	
}
