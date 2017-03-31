package com.cloudwalk.common.base.gzip;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class GzipFilter implements Filter {
	private final String ENABLE = "enable";
	private final String GZIP = "gzip";
	
	private boolean enable = true;
	
	public void destroy() {
	}
	
	public void doFilter(ServletRequest rq, ServletResponse rp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)rq;
		HttpServletResponse response = (HttpServletResponse)rp;
		
		if ((this.enable) && (isGZipEncoding(request))) {
			GzipResponse zipResponse = new GzipResponse(response);
			response.setHeader("Content-Encoding", GZIP);
			chain.doFilter(request, zipResponse);
			zipResponse.flush();
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig config) throws ServletException {
		String enable = config.getInitParameter(ENABLE);
		if (null == enable) {
			this.enable = true;
		} else {
			this.enable = Boolean.valueOf(enable).booleanValue();
		}
	}

	private boolean isGZipEncoding(HttpServletRequest request) {
		boolean flag = false;
		String encoding = request.getHeader("Accept-Encoding");
		if ((!StringUtils.isEmpty(encoding)) && (encoding.indexOf(GZIP) != -1)) {
			flag = true;
		}
		
		return flag;
	}
}