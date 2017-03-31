package com.cloudwalk.common.base.gzip;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class GzipResponse extends HttpServletResponseWrapper {
	private GzipStream stream;
	private PrintWriter writer;
	protected int len;

	public GzipResponse(HttpServletResponse response)
			throws IOException {
		super(response);
		this.stream = new GzipStream(response.getOutputStream());
	}

	public GzipResponse(HttpServletResponse response, GzipStream stream) throws IOException {
		super(response);
		this.stream = stream;
	}

	public void flush() throws IOException {
		if (this.writer != null) {
			this.writer.flush();
		}
		this.stream.finish();
	}

	public ServletOutputStream getOutputStream() throws IOException {
	  return this.stream;
	}

	public GzipStream getStream() {
		return this.stream;
	}

	public PrintWriter getWriter() throws IOException {
		if (this.writer == null) {
			this.writer = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
		}
		return this.writer;
	}

	public void setContentLength(int len) {
		this.len = len;
	}

	public void setStream(GzipStream stream) {
		this.stream = stream;
	}
}