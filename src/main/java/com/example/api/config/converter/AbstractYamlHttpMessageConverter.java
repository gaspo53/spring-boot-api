/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.config.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

/**
 * @author gaspar
 *
 */
public abstract class AbstractYamlHttpMessageConverter<T> extends AbstractHttpMessageConverter<T> {

	public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

	private final List<Charset> availableCharsets;
	private boolean writeAcceptCharset = true;

	protected AbstractYamlHttpMessageConverter() {
		super(new MediaType("application", "yaml"));
		this.availableCharsets = new ArrayList<Charset>(Charset
				.availableCharsets().values());
	}

	public void setWriteAcceptCharset(boolean writeAcceptCharset) {
		this.writeAcceptCharset = writeAcceptCharset;
	}

	@Override
	protected T readInternal(Class<? extends T> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		return this.readFromSource(clazz, inputMessage.getHeaders(),
				new InputStreamReader(inputMessage.getBody(),
						this.getCharset(inputMessage.getHeaders())));
	}

	@Override
	protected void writeInternal(T t, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {

		if (this.writeAcceptCharset) {
			outputMessage.getHeaders().setAcceptCharset(this.getAcceptedCharsets());
		}

		this.writeToResult(t, outputMessage.getHeaders(),
				new OutputStreamWriter(outputMessage.getBody(),
						this.getCharset(outputMessage.getHeaders())));
	}

	protected List<Charset> getAcceptedCharsets() {
		return this.availableCharsets;
	}

	private Charset getCharset(HttpHeaders headers) {
		MediaType contentType = headers.getContentType();
		return contentType.getCharSet() != null ? contentType.getCharSet()
				: DEFAULT_CHARSET;
	}

	protected abstract T readFromSource(Class<? extends T> clazz,
			HttpHeaders headers, Reader source) throws IOException;

	protected abstract void writeToResult(T t, HttpHeaders headers,
			Writer result) throws IOException;

}