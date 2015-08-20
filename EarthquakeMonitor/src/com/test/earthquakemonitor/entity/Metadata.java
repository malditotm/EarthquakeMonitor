package com.test.earthquakemonitor.entity;

public class Metadata{
	private long generated;
	private String url;
	private String title;
	private String api;
	private Integer count;
	private Integer status;
	
	public long getGenerated() {
		return generated;
	}
	public void setGenerated(long generated) {
		this.generated = generated;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getApi() {
		return api;
	}
	public void setApi(String api) {
		this.api = api;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
}
