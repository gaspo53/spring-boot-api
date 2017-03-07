/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.config.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.yaml.snakeyaml.Yaml;

/**
 * @author gaspar
 *
 */
public class JavaBeanSnakeYamlHttpMessageConverter extends AbstractSnakeYamlHttpMessageConverter<Object> {

	public JavaBeanSnakeYamlHttpMessageConverter() {
		super();
	}

	@Override
	protected Object readFromSource(Class<?> clazz, HttpHeaders headers,
			Reader source) throws IOException {
		Yaml beanLoader = this.getSnakeYamlInterface();
		return beanLoader.loadAs(source, clazz);
	}

	@Override
	protected void writeToResult(Object t, HttpHeaders headers, Writer result)
			throws IOException {

		Yaml beanDumpper = this.getSnakeYamlInterface();
		String yamlMessage = beanDumpper.dumpAsMap(t);
		FileCopyUtils.copy(yamlMessage, result);

	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return this.canRead(mediaType);
	}

	@Override
	public boolean canWrite(Class<?> clazz, MediaType mediaType) {
		return this.canWrite(mediaType);
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		// should not be called, since we override canRead/Write
		throw new UnsupportedOperationException();
	}

}