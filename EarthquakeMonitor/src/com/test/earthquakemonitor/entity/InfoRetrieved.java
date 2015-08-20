package com.test.earthquakemonitor.entity;

public class InfoRetrieved {
	private Long id;
	private String infoFromServer;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInfoFromServer() {
		return infoFromServer;
	}
	public void setInfoFromServer(String infoFromServer) {
		this.infoFromServer = infoFromServer;
	}
	public String toString() {
		return "InfoRetrieved [id=" + id + ", infoFromServer=" + infoFromServer
				+ "]";
	}
}
