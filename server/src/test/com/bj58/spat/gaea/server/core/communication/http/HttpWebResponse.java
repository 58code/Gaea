package com.bj58.spat.gaea.server.core.communication.http;

import java.util.Map;

public class HttpWebResponse {
	
	private int statusCode;
	private Map<String, String> headers;
	private byte[] headBuffer;
	private byte[] contentBuffer;
//	private HttpWebRequest httpRequest;
	

	public HttpWebResponse(byte[] headBuffer, byte[] contentBuffer, Map<String, String> headers) {
		this.setHeadBuffer(headBuffer);
		this.setContentBuffer(contentBuffer);
		this.setHeaders(headers);
		this.setStatusCode(Integer.parseInt(headers.get("StatusCode")));
	}
	
	

	public void close() throws Exception {
//		httpRequest.close();
	}
	
	
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

//	public HttpWebRequest getHttpRequest() {
//		return httpRequest;
//	}
//	public void setHttpRequest(HttpWebRequest httpRequest) {
//		this.httpRequest = httpRequest;
//	}
	
	public byte[] getHeadBuffer() {
		return headBuffer;
	}


	public void setHeadBuffer(byte[] headBuffer) {
		this.headBuffer = headBuffer;
	}


	public byte[] getContentBuffer() {
		return contentBuffer;
	}


	public void setContentBuffer(byte[] contentBuffer) {
		this.contentBuffer = contentBuffer;
	}
}