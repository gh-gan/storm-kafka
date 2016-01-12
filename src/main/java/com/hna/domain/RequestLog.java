package com.hna.domain;

public class RequestLog {
	
	/**
	 * {
	 * "website":"JDBJPRD000",
	 * "time_local":"02/Dec/2015:23:59:02 +0800",
	 * "remote_addr":"120.52.73.1",
	 * "remote_port":"41494",
	 * "request":"/403.html",
	 * "request_body":"dstCity=PEK&dstCity_m=&orgCity_m=&flightDate=2016-01-07&orgCity=LHW&tripType=ONEWAY&returnDate=",
	 * "response_time":"2015-12-02 23:59:02", 
	 * "request_time":"",
	 * "status":"403",
	 * "body_bytes_sent":"2754",
	 * "http_referer":"http://www.jdair.net/index.jsp",
	 * "http_user_agent":"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.154 Safari/537.36 LBBROWSER",
	 * "headers":"",
	 * "jsession":"-",
	 * "http_cookie":"-",
	 * "resp_body":"" 
	 * }
	 */
	private String website;
	private String time_local;
	private String remote_addr;
	private String remote_port;
	private String request;
	private String request_body;
	private String response_time;
	private String request_time;
	private String status;
	private String body_bytes_sent;
	private String http_referer;
	private String http_user_agent;
	private String headers;
	private String jsession;
	private String http_cookie;
	private String resp_body;
	
	public String getResp_body() {
		return resp_body;
	}
	public void setResp_body(String resp_body) {
		this.resp_body = resp_body;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getTime_local() {
		return time_local;
	}
	public void setTime_local(String time_local) {
		this.time_local = time_local;
	}
	public String getRemote_addr() {
		return remote_addr;
	}
	public void setRemote_addr(String remote_addr) {
		this.remote_addr = remote_addr;
	}
	public String getRemote_port() {
		return remote_port;
	}
	public void setRemote_port(String remote_port) {
		this.remote_port = remote_port;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	public String getRequest_body() {
		return request_body;
	}
	public void setRequest_body(String request_body) {
		this.request_body = request_body;
	}
	public String getResponse_time() {
		return response_time;
	}
	public void setResponse_time(String response_time) {
		this.response_time = response_time;
	}
	public String getRequest_time() {
		return request_time;
	}
	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBody_bytes_sent() {
		return body_bytes_sent;
	}
	public void setBody_bytes_sent(String body_bytes_sent) {
		this.body_bytes_sent = body_bytes_sent;
	}
	public String getHttp_referer() {
		return http_referer;
	}
	public void setHttp_referer(String http_referer) {
		this.http_referer = http_referer;
	}
	public String getHttp_user_agent() {
		return http_user_agent;
	}
	public void setHttp_user_agent(String http_user_agent) {
		this.http_user_agent = http_user_agent;
	}
	public String getHeaders() {
		return headers;
	}
	public void setHeaders(String headers) {
		this.headers = headers;
	}
	public String getJsession() {
		return jsession;
	}
	public void setJsession(String jsession) {
		this.jsession = jsession;
	}
	public String getHttp_cookie() {
		return http_cookie;
	}
	public void setHttp_cookie(String http_cookie) {
		this.http_cookie = http_cookie;
	}
	
	
}
