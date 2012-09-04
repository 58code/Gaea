/*
 *  Copyright Beijing 58 Information Technology Co.,Ltd.
 *
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.bj58.spat.gaea.server.core.communication.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipelineCoverage;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpChunk;
import org.jboss.netty.handler.codec.http.HttpHeaders;
import org.jboss.netty.handler.codec.http.HttpMethod;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.handler.codec.http.HttpVersion;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import com.bj58.sfft.json.JsonHelper;
import com.bj58.spat.gaea.protocol.sdp.RequestProtocol;
import com.bj58.spat.gaea.protocol.sfp.v1.Protocol;
import com.bj58.spat.gaea.protocol.utility.KeyValuePair;
import com.bj58.spat.gaea.server.contract.annotation.HttpRequestMethod;
import com.bj58.spat.gaea.server.contract.context.Global;
import com.bj58.spat.gaea.server.contract.context.GaeaChannel;
import com.bj58.spat.gaea.server.contract.context.GaeaContext;
import com.bj58.spat.gaea.server.contract.context.ServerType;
import com.bj58.spat.gaea.server.contract.log.ILog;
import com.bj58.spat.gaea.server.contract.log.LogFactory;
import com.bj58.spat.gaea.server.contract.server.IServerHandler;
import com.bj58.spat.gaea.server.core.communication.http.Action.Parameter;
import com.bj58.spat.gaea.server.util.ExceptionHelper;
import com.bj58.spat.gaea.server.util.Util;

/**
 * ServerStateType
 * 
 * @author Service Platform Architecture Team (spat@58.com)
 */
@ChannelPipelineCoverage("one")
public class HttpHandler extends SimpleChannelUpstreamHandler implements IServerHandler {

	/**
	 * logger
	 */
	private static ILog logger = LogFactory.getLogger(HttpHandler.class);
	
	/**
	 * http connection count
	 */
	private static volatile int connectedCount = 0;
	
	/**
	 * default encoding:utf-8
	 */
	private static final String DEFAULT_ENCODING = "utf-8";

	private volatile boolean readingChunks;
	private volatile HttpRequest request;
	private volatile HttpContext context = new HttpContext();
	private volatile byte[] byteContent = null;
	private volatile int contentLength = 0;
	private volatile int receiveLength = 0;

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        if (!readingChunks) {
            request = (HttpRequest) e.getMessage();
            String requestUri = request.getUri();
            if(requestUri.lastIndexOf("favicon.ico") > 0 ){
            	logger.info("this url is favicon.ico");
            	return;
            }
            //uri
            context.setUri(requestUri);
            //http method
            if(request.getMethod() == HttpMethod.GET) {
            	context.setMethod(HttpRequestMethod.GET);
            } else if(request.getMethod() == HttpMethod.POST) {
            	context.setMethod(HttpRequestMethod.POST);
            } else if(request.getMethod() == HttpMethod.DELETE) {
            	context.setMethod(HttpRequestMethod.DELETE);
            } else if(request.getMethod() == HttpMethod.PUT) {
            	context.setMethod(HttpRequestMethod.PUT);
            } else if(request.getMethod() == HttpMethod.HEAD) {
            	context.setMethod(HttpRequestMethod.HEAD);
            }

            //headers
            Map<String, List<String>> headers = new HashMap<String, List<String>>();
            if (!request.getHeaderNames().isEmpty()) {
                for (String name: request.getHeaderNames()) {
                	List<String> vList = new ArrayList<String>();
                    for (String value: request.getHeaders(name)) {
                    	vList.add(value);
                    }
                    headers.put(name, vList);
                }
            }
            context.setHeaders(headers);

            //params
            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri(), DEFAULT_ENCODING);
            Map<String, List<String>> params = queryStringDecoder.getParameters();
            context.setParams(params);
            
            //ip
            context.setFromIP(e.getChannel().getRemoteAddress().toString());
            context.setToIP(e.getChannel().getLocalAddress().toString());

            if (request.isChunked()) {
                readingChunks = true;
            } else {
    			contentLength = (int) request.getContentLength();
    			if(contentLength > 0) {
    				byteContent = new byte[contentLength];
    				ChannelBuffer cb = request.getContent();
    				if(cb != null) {
    					cb.getBytes(0, byteContent);
    				}
    			}
    			
    			//content buffer
                context.setContentBuffer(byteContent);
                invoke(context, e);
            }
        } else {
            HttpChunk chunk = (HttpChunk) e.getMessage();
    		ChannelBuffer cb = chunk.getContent();
    		if(cb != null && cb.capacity()>0) {
	    		cb.getBytes(0, byteContent, receiveLength, cb.capacity());
	    		this.receiveLength += cb.capacity();
    		}
    		
            if (chunk.isLast()) {
                readingChunks = false;
                //content buffer
                context.setContentBuffer(byteContent);
                invoke(context, e);
            }
        }
    }
    
    /**
     * create GaeaContext and invoke real service
     * @param httpContext
     * @param e
     */
    private void invoke(HttpContext httpContext, MessageEvent e) {
    	try {
    		logger.debug("begin get action");
    		
    		Action action = RequestMapping.getAction(httpContext);
    		List<KeyValuePair> paraKVList = new ArrayList<KeyValuePair>();
    		for(Parameter para : action.getParamList()) {
    			paraKVList.add(new KeyValuePair(Util.getSimpleParaName(para.getType()), para.getValue()));
    		}
    		
	    	GaeaContext gaeaContext = new GaeaContext();
	    	gaeaContext.setServerType(ServerType.HTTP);
	    	gaeaContext.setServerHandler(this);
	    	gaeaContext.setChannel(new GaeaChannel(e.getChannel()));
			/**协议*/
			Protocol protocol = new Protocol();
			RequestProtocol request = new RequestProtocol();
			request.setLookup(action.getLookup());
			request.setMethodName(action.getMethodName());
			request.setParaKVList(paraKVList);
			protocol.setSdpEntity(request);
			gaeaContext.getGaeaRequest().setProtocol(protocol);
			/**HttpContext 上下文*/
			com.bj58.spat.gaea.server.contract.http.HttpContext httpcontext = new com.bj58.spat.gaea.server.contract.http.HttpContext();
			com.bj58.spat.gaea.server.contract.http.HttpRequest httpRequest = new com.bj58.spat.gaea.server.contract.http.HttpRequest();
			httpRequest.setContent(httpContext.getContentBuffer());
			httpRequest.setFromIP(httpContext.getFromIP());
			httpRequest.setToIP(httpContext.getToIP());
			httpRequest.setUri(httpContext.getUri());
			httpRequest.setHeaders_(httpContext.getHeaders());
			httpcontext.setRequest(httpRequest);
			gaeaContext.setHttpContext(httpcontext);
			
			HttpServer.invokerHandle.invoke(gaeaContext);
			
    	} catch (Throwable ex) {
    		logger.error("http request error!!!", ex);
    		
    		HttpResponseStatus status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
    		if(ex instanceof HttpException) {
    			if(((HttpException)ex).getErrorCode() == 404) {
    				status = HttpResponseStatus.NOT_FOUND;
    			}
    		}
    		if(HttpServer.errorPageHTML != null) {
    			writeResponse(ChannelBuffers.copiedBuffer(HttpServer.errorPageHTML, DEFAULT_ENCODING), 
    					e.getChannel(), 
    					status);
    		} else {
    			String errorMsg = ExceptionHelper.getStackTrace(ex);
    			writeResponse(ChannelBuffers.copiedBuffer(errorMsg, DEFAULT_ENCODING), 
    					e.getChannel(), 
    					status);
    		}
    	}
    }
    
    /**
     * write response data to client
     * @param responseBuffer
     * @param e
     */
    private void writeResponse(ChannelBuffer buffer, Channel channel, HttpResponseStatus status) {
        // Decide whether to close the connection or not.
        boolean close =
            HttpHeaders.Values.CLOSE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION)) ||
            request.getProtocolVersion().equals(HttpVersion.HTTP_1_0) &&
            !HttpHeaders.Values.KEEP_ALIVE.equalsIgnoreCase(request.getHeader(HttpHeaders.Names.CONNECTION));

        // Build the response object.
        HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, status);

        response.setContent(buffer);
        if(status != HttpResponseStatus.OK) {
        	response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/html; charset=" + DEFAULT_ENCODING);
        } else {
        	response.setHeader(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=" + DEFAULT_ENCODING);
        }
        response.setHeader(HttpHeaders.Names.CONTENT_LENGTH, String.valueOf(buffer.readableBytes()));
        response.setHeader(HttpHeaders.Names.CONNECTION, "close");
        response.setHeader(HttpHeaders.Names.SERVER, "Gaea");
        
        // Write the response.
        ChannelFuture future = channel.write(response);

        // Close the connection after the write operation is done if necessary.
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
    
	
	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
		connectedCount++;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		logger.error("http channel exception(" + 
					e.getChannel().getRemoteAddress().toString() + 
					")",
					e.getCause());
		e.getChannel().close();
	}
	
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
		connectedCount--;
		e.getChannel().close();
	}

	@Override
	public void writeResponse(GaeaContext context) {
		try {
			String jsonStr = JsonHelper.toJsonExt(context.getGaeaResponse().getReturnValue());
			context.getGaeaResponse().setResponseBuffer(jsonStr.getBytes(Global.getSingleton().getServiceConfig().getString("gaea.encoding")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//write response data to client 
		HttpResponseStatus status = HttpResponseStatus.OK;
		ChannelBuffer buffer = null;
		if(context.getError() != null) {
			status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
		}
		if(HttpServer.errorPageHTML != null && context.getError() != null) {
			buffer = ChannelBuffers.copiedBuffer(HttpServer.errorPageHTML, DEFAULT_ENCODING);
		} else {
		    buffer = ChannelBuffers.copiedBuffer(context.getGaeaResponse().getResponseBuffer());
		}
		writeResponse(buffer, context.getChannel().getNettyChannel(), status);
	}
}