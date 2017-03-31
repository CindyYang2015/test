package com.cloudwalk.common.base.gzip;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletOutputStream;

public class GzipStream extends ServletOutputStream {
	private GZIPOutputStream zipStream;

	public GzipStream(OutputStream out)
		throws IOException {
		this.zipStream = new GZIPOutputStream(out);
	}

	public void close() throws IOException {
		this.zipStream.close();
	}

	public void finish() throws IOException {
		this.zipStream.finish();
	}

	public void flush() throws IOException {
		this.zipStream.flush();
	}

	public boolean isReady() {
		return false;
	}

	public void write(byte[] b) throws IOException {
		this.zipStream.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		this.zipStream.write(b, off, len);
	}

	public void write(int arg0) throws IOException {
		this.zipStream.write(arg0);
	}
}