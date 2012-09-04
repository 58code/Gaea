package com.bj58.spat.gaea.server.core.communication.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HttpWebRequest {
	
	private HttpMethod method;
	private String contentType;
	private int contentLength;
	private int timeOut;
	private Map<String, String> headers = new HashMap<String, String>();
	private String host;
	private String relativeURL;
	
	private Socket socket;
    private BufferedInputStream socketInputStream;
    private BufferedOutputStream socketOutputStream;
    
    
    private static final int CONNECT_TIMEOUT = 3000;
    private static final String DEFAULT_ENCODING = "utf-8";
    
    private HttpWebRequest() {
    	
    }

    public static HttpWebRequest create(String url) throws Exception {
    	URL tempUrl = new URL(url);
    	HttpWebRequest httpRequest = new HttpWebRequest();
		httpRequest.setHost(tempUrl.getHost());
		if(tempUrl.getPath() == null || tempUrl.getPath().equalsIgnoreCase("")) {
			httpRequest.setRelativeURL("/");
		} else {
			httpRequest.setRelativeURL(tempUrl.getPath());
		}

    	httpRequest.socket = new Socket();
    	httpRequest.socket.setSoTimeout(httpRequest.getTimeOut());
    	httpRequest.socket.setTcpNoDelay(true);
    	httpRequest.socket.setSendBufferSize(1024 * 128);
    	httpRequest.socket.setReceiveBufferSize(1024);
        
        SocketAddress address = new InetSocketAddress(InetAddress.getByName(httpRequest.getHost()), tempUrl.getPort());
        httpRequest.socket.connect(address, CONNECT_TIMEOUT);

        // wrap streams
        httpRequest.socketInputStream = new BufferedInputStream(httpRequest.socket.getInputStream());
        httpRequest.socketOutputStream = new BufferedOutputStream(httpRequest.socket.getOutputStream());
        
    	return httpRequest;
    }
    
    
    public void write(byte[] buffer) throws IOException {
    	byte[] sendData = createHttpProtocol(buffer);
    	socketOutputStream.write(sendData);
    	socketOutputStream.flush();
    }
    
    
	public HttpWebResponse getResponse() throws Exception {
    	ByteArrayOutputStream receiveData = new ByteArrayOutputStream();
    	
    	byte[] headBuffer;
    	byte[] contentBuffer;
    	byte[] crlf = new byte[4];
    	int markIndex = 0;
    	int index = 0;
    	Map<String, String> headers = null;
    	
    	while(true) {
    		int data = socketInputStream.read();
    		receiveData.write(data);
    		
    		if(markIndex < 3) {
    			markIndex ++;
    		} else {
    			markIndex = 0;
    		}
    		crlf[markIndex] = (byte)data;
    		
    		boolean headEnd = true;
    		for(byte b : crlf) {
    			if(b != '\r' && b != '\n') {
    				headEnd = false;
    			}
    		}
    		if(headEnd) {
    			headBuffer = receiveData.toByteArray();
    			headers = getHeads(headBuffer);
    			int contentLength = Integer.parseInt(headers.get("Content-Length"));
    			contentBuffer = new byte[contentLength];
    			socketInputStream.read(contentBuffer);
    			break;
    		}
    		index++;
    	}

    	HttpWebResponse response = new HttpWebResponse(headBuffer, contentBuffer, headers);
    	return response;
    }
	
	
	private Map<String, String> getHeads(byte[] headBuffer) throws Exception{
		ByteArrayInputStream inputStream = null;
		BufferedReader reader = null;
		Map<String, String> headers = new HashMap<String, String>();
		try {
			inputStream = new ByteArrayInputStream(headBuffer);
			reader = new BufferedReader(new InputStreamReader(inputStream));
			while(true) {
				String line = reader.readLine();
				if(line != null && !line.equalsIgnoreCase("")) {
					String[] head = line.split(":");
					if(head.length == 1) {
						head = line.split(" ");
						headers.put("StatusCode", head[1]);
					} else {
						headers.put(head[0].trim(), head[1].trim());
					}
				} else {
					break;
				}
			}
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(inputStream != null) {
				try {
					inputStream.close();
				} catch(Exception ex) {
					
				}
			}
			
			if(reader != null) {
				try {
					reader.close();
				} catch(Exception ex) {
					
				}
			}
		}
		
		return headers;
	}
	
	
	@SuppressWarnings("rawtypes")
	private byte[] createHttpProtocol(byte[] bufferData) throws UnsupportedEncodingException {
		//组织HTTP头
		StringBuffer sb = new StringBuffer();
		
		sb.append(this.getMethod().toString());
		sb.append(" ");
		sb.append(this.getRelativeURL());
		sb.append(" HTTP/1.1\r\n");
		
		sb.append("Content-Type: ");
		sb.append(this.getContentType());
		sb.append("\r\n");
		
	    sb.append("Accept: image/jpeg, application/x-ms-application, image/gif, application/xaml+xml, image/pjpeg, application/x-ms-xbap, application/x-shockwave-flash, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n");
	    sb.append("Accept-Language: zh-cn\r\n");
	    sb.append("User-Agent: Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.1; Trident/4.0; EmbeddedWB 14.52 from: http://www.bsalsa.com/ EmbeddedWB 14.52; SLCC2; .NET CLR 2.0.50727; .NET CLR 3.5.30729; .NET CLR 3.0.30729; Media Center PC 6.0; InfoPath.1)\r\n");
	    sb.append("Accept-Encoding: gzip, deflate\r\n");
	    
	    sb.append("Host: ");
	    sb.append(this.getHost());
	    sb.append("\r\n");
	    
	    sb.append("Connection: Keep-Alive\r\n");
	    
	    if(bufferData != null) {
	    	sb.append("Content-Length: " + bufferData.length + "\r\n");
	    } else {
	    	sb.append("Content-Length: 0\r\n");
	    }
	    
	    
		Iterator it = this.getHeaders().entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			sb.append(entry.getKey());
			sb.append(": ");
			sb.append(entry.getValue());
			sb.append("\r\n");
		}
	    
	    sb.append("\r\n");
	    
	    byte[] headBuffer = sb.toString().getBytes(DEFAULT_ENCODING);
	    byte[] allBuffer = null;
	    
	    if(bufferData != null) {
		    allBuffer = new byte[headBuffer.length + bufferData.length];
		    System.arraycopy(headBuffer, 0, allBuffer, 0, headBuffer.length);
		    System.arraycopy(bufferData, 0, allBuffer, headBuffer.length, bufferData.length);
	    } else {
	    	allBuffer = new byte[headBuffer.length];
		    System.arraycopy(headBuffer, 0, allBuffer, 0, headBuffer.length);
	    }

	    return allBuffer;
	}
	
	
	public void close() throws Exception {
		if(socketInputStream != null) {
            try{
                socketInputStream.close();
                socketInputStream = null;
            } catch(Exception ex){
                throw ex;
            }
        }

        if(socketOutputStream != null) {
            try{
                socketOutputStream.close();
                socketOutputStream = null;
            } catch(Exception ex){
                throw ex;
            }
        }
        
        if(socket != null) {
        	try{
        		socket.close();
        		socket = null;
            } catch(Exception ex){
                throw ex;
            }
        }
	}
    
    
    public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getRelativeURL() {
		return relativeURL;
	}

	public void setRelativeURL(String relativeURL) {
		this.relativeURL = relativeURL;
	}
}