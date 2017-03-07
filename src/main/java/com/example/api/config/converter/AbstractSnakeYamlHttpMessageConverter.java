/**
 * 
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 */
package com.example.api.config.converter;

import org.yaml.snakeyaml.Yaml;

/**
 * @author gaspar
 *
 */
public abstract class AbstractSnakeYamlHttpMessageConverter<T> extends
		AbstractYamlHttpMessageConverter<T> {

	// SnakeYaml requires one instance for each Thread so ThreadLocal is used.
	private ThreadLocal<Yaml> yamlInterfaces = new ThreadLocal<Yaml>();

	public AbstractSnakeYamlHttpMessageConverter() {
		super();
	}

	protected final Yaml getSnakeYamlInterface() {

		Yaml yaml = this.yamlInterfaces.get();
		if (yaml == null) {
			yaml = new Yaml();
			this.yamlInterfaces.set(yaml);
		}

		return yaml;
	}

}