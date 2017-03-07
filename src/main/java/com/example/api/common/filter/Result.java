package com.example.api.common.filter;

import java.util.Collection;

/**
 * Entity to serve as a Result container from a Database query.<br>
 * Also handle pagination, using {@link Page}
 * 
 * @author Gaspar Rajoy - <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 * @param <T>
 */
public class Result<T> {

	private static final int PAGINATOR_SIZE = 10;

	private Collection<T> result;
	
	private long totalResults;
	
	private Page page;

	//Pagination properties
	/**
	 * Number of pages to show in pagination bar (optional)
	 */
	private int paginatorSize;
	
	private int actualOffset;
	
	private int startOffset;
	
	private long beginPage;
	
	private long remainingPages;
	
	private long endPage;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Result [result=");
		builder.append(this.result);
		builder.append(", totalResults=");
		builder.append(this.totalResults);
		builder.append(", page=");
		builder.append(this.page);
		builder.append(", paginatorSize=");
		builder.append(this.paginatorSize);
		builder.append(", actualOffset=");
		builder.append(this.actualOffset);
		builder.append(", startOffset=");
		builder.append(this.startOffset);
		builder.append(", beginPage=");
		builder.append(this.beginPage);
		builder.append(", remainingPages=");
		builder.append(this.remainingPages);
		builder.append(", endPage=");
		builder.append(this.endPage);
		builder.append("]");
		return builder.toString();
	}

	public Result(){
		this.setPaginatorSize(PAGINATOR_SIZE);
	}
	
	public long getTotalPages() {
		long ret;
		if (this.page.getPageSize() > 0){
			if(this.totalResults % this.page.getPageSize() == 0){
				ret = this.totalResults / this.page.getPageSize();
			} else {
				ret = (this.totalResults / this.page.getPageSize()) + 1;
			}
		}else{
			ret = 1;
		}
		return ret;
	}
	
	//Private methods
	private void initializePagination(){
		//Pages
		int pageNumber = this.getPage().getPageNumber();
		this.actualOffset = Integer.divideUnsigned(pageNumber, this.getPaginatorSize());
		this.startOffset = pageNumber % this.getPaginatorSize();
		this.beginPage = (this.actualOffset * this.getPaginatorSize()) + 1;
		this.remainingPages = this.getTotalPages() - this.getBeginPage();
		this.endPage = ((this.getBeginPage() + this.getPaginatorSize()) <= this.getTotalPages())
						?(this.getBeginPage() + this.getPaginatorSize())
					    :(this.getTotalPages());
		//TODO ugly thing
		this.beginPage = ( ((this.getTotalPages() - this.getBeginPage()) < this.getPaginatorSize())
						   && (this.getTotalPages() > this.getPaginatorSize()))
					 	 ?(this.getBeginPage() - this.getPaginatorSize() + this.getStartOffset())
					 	 :(this.getBeginPage());
	}

	//Getters & setters
	public Collection<T> getResult() {
		return this.result;
	}

	public void setResult(Collection<T> result) {
		this.result = result;
		this.initializePagination();
	}

	 public long getTotalResults() {
		return this.totalResults;
	}

	public void setTotalResults(long totalResults) {
		this.totalResults = totalResults;
		if (this.page != null) {
			this.page.setTotalResults(totalResults);
		}
	}

	public Page getPage() {
		return this.page;
	}

	public void setPage(Page page) {
		this.page = page;
	}


	public int getPaginatorSize() {
		return this.paginatorSize;
	}


	public void setPaginatorSize(int paginatorSize) {
		this.paginatorSize = paginatorSize;
	}

	public int getActualOffset() {
		return this.actualOffset;
	}

	public void setActualOffset(int actualOffset) {
		this.actualOffset = actualOffset;
	}

	public int getStartOffset() {
		return this.startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public long getBeginPage() {
		return this.beginPage;
	}

	public void setBeginPage(long beginPage) {
		this.beginPage = beginPage;
	}

	public long getEndPage() {
		return this.endPage;
	}

	public void setEndPage(long endPage) {
		this.endPage = endPage;
	}

	public long getRemainingPages() {
		return this.remainingPages;
	}

	public void setRemainingPages(long remainingPages) {
		this.remainingPages = remainingPages;
	}

}
