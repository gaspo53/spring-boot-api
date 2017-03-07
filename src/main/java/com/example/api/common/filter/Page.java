package com.example.api.common.filter;

/**
 * Entity to Model a Page; for pagination purposes
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */
public class Page {

	/**
	 * Requesting page number
	 */
	private int pageNumber=1;
	
	/**
	 * Page size
	 */
	private int pageSize=-1;

	private boolean totalResultsCached = false;
	
	private long totalResults = 0;

	public Page(int pageNumber, int pageSize) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
	}

	public Page(int pageNumber, int pageSize, boolean totalResultsCached) {
		super();
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.totalResultsCached = totalResultsCached;
	}	

	public Page() {
		this(1,0);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Page [pageNumber=");
		builder.append(this.pageNumber);
		builder.append(", pageSize=");
		builder.append(this.pageSize);
		builder.append(", totalResultsCached=");
		builder.append(this.totalResultsCached);
		builder.append(", totalResults=");
		builder.append(this.totalResults);
		builder.append("]");
		return builder.toString();
	}

	// Getters & setters
	public int getPageNumber() {
		return this.pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalResults() {
		return this.totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
	}

	public boolean isTotalResultsCached() {
		return this.totalResultsCached;
	}

	public void setTotalResultsCached(boolean totalResultsCached) {
		this.totalResultsCached = totalResultsCached;
	}
}
