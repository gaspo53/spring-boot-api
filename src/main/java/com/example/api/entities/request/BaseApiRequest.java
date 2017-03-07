package com.example.api.entities.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import com.example.api.common.utils.LogHelper;
import com.example.api.entities.PostableData;
import com.example.api.enums.ApiComponent;
import com.example.api.enums.BaseProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

/**
 * Attributes that all API requests must have
 *
 */
public abstract class BaseApiRequest implements Serializable {

    private static final long serialVersionUID = 6381341491939888291L;
    public static final String SORTING = "sorting";

    @JsonIgnore
    private String country;

    @JsonIgnore
    private Locale currentLocale;

    @JsonIgnore
    private BaseProduct product;

    @JsonIgnore
    private ApiComponent component;

    /**
     * Tell if this request can be cached. Uses {@link #cacheTtl}
     */
    @JsonIgnore
    private boolean cacheable;

    /**
     * Indicates for how many seconds this request can be stored in cache.
     */
    @JsonIgnore
    private Integer cacheTtl;

    /**
     * The naming strategy for the respone
     */
    @JsonIgnore
    private PropertyNamingStrategy responseFormat;
    
    @JsonIgnore
    private boolean isFormDataRequest;
    
    // Paging
    private Integer offset;
    private Integer limit;

    // Query parameters
    @JsonIgnore
    private List<NameValuePair> queryParameters;

    // Path values
    @JsonIgnore
    private List<String> pathValues;

    @JsonIgnore
    private String pathValueDelimiter;
    
    @JsonIgnore
    private String pathValuesOpenString;
    
    @JsonIgnore
    private String pathValuesCloseString;

    @JsonIgnore
    private boolean queued;
    
    @JsonIgnore
    private boolean forcedRequest;
    
    @JsonIgnore
    private String usedApiKey;
    
    // Additional headers
    @JsonIgnore
    private List<Header> headers;
    
    
    /**
     * Data to send into a POST/PATH/PUT
     */
    private PostableData requestData;
    
    
    /**
     * Whether needs the ApiComponent API base URL or not.
     */
    @JsonIgnore
    private boolean isApiContext;


    public BaseApiRequest() {
    	this.currentLocale = new Locale("es");
        this.offset = 0;
        this.setPathValueDelimiter("/");
        this.setQueryParameters(new ArrayList<NameValuePair>());
        this.setResponseFormat(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        this.setHeaders(new ArrayList<Header>());
        this.setPathValues(new ArrayList<String>());
        this.setFormDataRequest(false);
        this.setApiContext(true);
        this.setQueued(false);
        this.setForcedRequest(false);
        // this.setComponent(ApiComponent.DEFAULT);
    }

    public BaseApiRequest(String countryId, String locale) {
        this();
        this.country = countryId;
        this.currentLocale = new Locale(countryId, locale);
    }

    @Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BaseApiRequest [");
		if (this.country != null) {
			builder.append("country=");
			builder.append(this.country);
			builder.append(", ");
		}
		if (this.currentLocale != null) {
			builder.append("currentLocale=");
			builder.append(this.currentLocale);
			builder.append(", ");
		}
		if (this.product != null) {
			builder.append("product=");
			builder.append(this.product);
			builder.append(", ");
		}
		if (this.component != null) {
			builder.append("component=");
			builder.append(this.component);
			builder.append(", ");
		}
		builder.append("cacheable=");
		builder.append(this.cacheable);
		builder.append(", ");
		if (this.cacheTtl != null) {
			builder.append("cacheTtl=");
			builder.append(this.cacheTtl);
			builder.append(", ");
		}
		if (this.responseFormat != null) {
			builder.append("responseFormat=");
			builder.append(this.responseFormat);
			builder.append(", ");
		}
		builder.append("isFormDataRequest=");
		builder.append(this.isFormDataRequest);
		builder.append(", ");
		if (this.offset != null) {
			builder.append("offset=");
			builder.append(this.offset);
			builder.append(", ");
		}
		if (this.limit != null) {
			builder.append("limit=");
			builder.append(this.limit);
			builder.append(", ");
		}
		if (this.queryParameters != null) {
			builder.append("queryParameters=");
			builder.append(this.queryParameters);
			builder.append(", ");
		}
		if (this.pathValues != null) {
			builder.append("pathValues=");
			builder.append(this.pathValues);
			builder.append(", ");
		}
		if (this.pathValueDelimiter != null) {
			builder.append("pathValueDelimiter=");
			builder.append(this.pathValueDelimiter);
			builder.append(", ");
		}
		if (this.pathValuesOpenString != null) {
			builder.append("pathValuesOpenString=");
			builder.append(this.pathValuesOpenString);
			builder.append(", ");
		}
		if (this.pathValuesCloseString != null) {
			builder.append("pathValuesCloseString=");
			builder.append(this.pathValuesCloseString);
			builder.append(", ");
		}
		if (this.headers != null) {
			builder.append("headers=");
			builder.append(this.headers);
			builder.append(", ");
		}
		if (this.requestData != null) {
			builder.append("requestData=");
			builder.append(this.requestData);
			builder.append(", ");
		}
		builder.append("isApiContext=");
		builder.append(this.isApiContext);
		builder.append("]");
		return builder.toString();
	}

	public void addQueryParameter(String name, Object value) {
        if (StringUtils.isNotEmpty(name)) {
            String strValue = Objects.toString(value,"");
            if ((StringUtils.isNotEmpty(strValue)) && (!StringUtils.equalsIgnoreCase(strValue, "null") && (!StringUtils.equalsIgnoreCase(strValue, "NA")))) {
                if (!this.hasParameter(name)) {
                    NameValuePair param = new BasicNameValuePair(name, strValue);
                    CollectionUtils.addIgnoreNull(this.getQueryParameters(), param);
                }
            }
        }
    }

    public void addQueryParameter(String name, List<String> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            Iterator<String> valuesIterator = values.iterator();
            while (valuesIterator.hasNext()) {
                String value = valuesIterator.next();
                this.addQueryParameter(name, value);
            }
        }
    }

    public void addQueryParameter(String name, Enum<?> value) {
        try {
            String valueOf = Objects.toString(value, "");
            this.addQueryParameter(name, valueOf);
        } catch (Exception e) {
            LogHelper.warn(this, e);
        }
    }

    public void addHeader(String name, String value) {
        if ((StringUtils.isNotEmpty(value)) && (!StringUtils.equalsIgnoreCase(value, "NA"))) {
            Header header = new BasicHeader(name, value);
            if (!this.hasHeader(name)) {
                CollectionUtils.addIgnoreNull(this.getHeaders(), header);
            }
        }
    }

    public void addHeader(String name, Enum<?> value) {
        String valueOf = Objects.toString(value, "");
        this.addHeader(name, valueOf);
    }

    /**
     * Adds all headers from {@link HttpServletRequest} to {@link BaseApiRequest#headers}.<br>
     * A header must start with "X"
     * @param request
     */
    public void propagateHeaders(HttpServletRequest request) {
        try {
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (StringUtils.startsWithIgnoreCase(headerName, "X")) {
                    Enumeration<String> headers = request.getHeaders(headerName);
                    while (headers.hasMoreElements()) {
                        String headerValue = headers.nextElement();
                        this.addHeader(headerName, headerValue);
                    }
                }
            }
        } catch (Exception e) {
            LogHelper.info(this, e);
        }
    }

    public void addPathValue(String value) {
        if ((StringUtils.isNotEmpty(value)) && (!StringUtils.equalsIgnoreCase(value, "NA"))) {
            CollectionUtils.addIgnoreNull(this.getPathValues(), value);
        }
    }

    public void addPathValue(Enum<?> value) {
        try {
            String valueOf = Objects.toString(value, "");
            this.addPathValue(valueOf);
        } catch (Exception e) {
            LogHelper.warn(this, e);
        }
    }

    public void removeParam(String name, String value) {
        BasicNameValuePair nameValuePair = new BasicNameValuePair(name, value);
        this.getQueryParameters().remove(nameValuePair);
    }

    public void removeParam(String name) {
        Set<NameValuePair> paramsToRemove = new HashSet<NameValuePair>();

        Iterator<NameValuePair> iterator = this.getQueryParameters().iterator();
        while (iterator.hasNext()) {
            NameValuePair param = iterator.next();
            if (StringUtils.equalsIgnoreCase(param.getName(), name)) {
                paramsToRemove.add(param);
            }
        }

        this.getQueryParameters().removeAll(paramsToRemove);
    }

    /**
     * Tells if {@link #queryParameters} has an entry with the <b>name</b> and <b>value</b> received
     * @param name
     * @param value
     * @return
     */
    public boolean hasParameter(String name, Object value) {
        boolean hasParameter = false;

        String strValue = Objects.toString(value, "");

        NameValuePair nvp = new BasicNameValuePair(name, strValue);
        hasParameter = this.getQueryParameters().contains(nvp);

        return hasParameter;
    }


    /**
     * Tells if {@link #headers} has an entry with named with <b>name</b>
     * @param name
     * @param value
     * @return
     */
    public boolean hasHeader(String name) {
        boolean hasHeader = false;
        Set<String> set = new HashSet<String>();
        if (this.hasAdditionalHeaders()) {
            Iterator<Header> iterator = this.getHeaders().iterator();
            while (iterator.hasNext()) {
                Header header = iterator.next();
                set.add(header.getName());
            }
            hasHeader = set.contains(name);
        }
        return hasHeader;
    }

    /**
     * Tells if {@link #queryParameters} has an entry with named with <b>name</b>
     * @param name
     * @param value
     * @return
     */
    public boolean hasParameter(String name) {
        boolean hasParameter = false;
        Set<String> set = new HashSet<String>();
        if (this.hasAdditionalQueryParams()) {
            Iterator<NameValuePair> iterator = this.getQueryParameters().iterator();
            while (iterator.hasNext()) {
                NameValuePair param = iterator.next();
                set.add(param.getName());
            }
            hasParameter = set.contains(name);
        }
        return hasParameter;
    }

    /** Tells if the current ApiRequest has query parameters to add.
     * Override to get more control over it.
     * @return
     */
    public boolean hasAdditionalQueryParams() {
        return CollectionUtils.isNotEmpty(this.getQueryParameters());
    }

    /** Tells if the current ApiRequest has headers to add.
     * Override to get more control over it.
     * @return
     */
    public boolean hasAdditionalHeaders() {
        return CollectionUtils.isNotEmpty(this.getHeaders());
    }

    /** Tells if the current ApiRequest has query parameters to add.
     * Override to get more control over it.
     * @return
     */
    public boolean hasPathValues() {
        return CollectionUtils.isNotEmpty(this.getPathValues());
    }

    // Getters and setters
    public String getCountry() {
        return this.country;
    }

    public void setCountry(String countryId) {
        this.country = countryId;
    }

    public Locale getCurrentLocale() {
        return this.currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
    }

    public BaseProduct getProduct() {
        return this.product;
    }

    public void setProduct(BaseProduct product) {
        this.product = product;
    }

    public Integer getOffset() {
        return this.offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public List<NameValuePair> getQueryParameters() {
        return this.queryParameters;
    }

    public void setQueryParameters(List<NameValuePair> queryParameters) {
        this.queryParameters = queryParameters;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    public List<String> getPathValues() {
        return this.pathValues;
    }

    public void setPathValues(List<String> pathValues) {
        this.pathValues = pathValues;
    }

    public boolean isCacheable() {
        return BooleanUtils.isTrue(this.cacheable);
    }

    public void setCacheable(boolean cacheable) {
        this.cacheable = cacheable;
    }

    public Integer getCacheTtl() {
        return this.cacheTtl;
    }

    public void setCacheTtl(Integer cacheTtl) {
        this.cacheTtl = cacheTtl;
    }

    public ApiComponent getComponent() {
        return this.component;
    }

    public void setComponent(ApiComponent component) {
        this.component = component;
    }

	public PostableData getRequestData() {
		return this.requestData;
	}

	public void setRequestData(PostableData requestData) {
		this.requestData = requestData;
	}

	@JsonIgnore
	public boolean isFormDataRequest() {
		return this.isFormDataRequest;
	}

	public void setFormDataRequest(boolean isFormDataRequest) {
		this.isFormDataRequest = isFormDataRequest;
	}

	@JsonIgnore
	public boolean isApiContext() {
		return this.isApiContext;
	}

	public void setApiContext(boolean isApiContext) {
		this.isApiContext = isApiContext;
	}

	public PropertyNamingStrategy getResponseFormat() {
		return this.responseFormat;
	}

	public void setResponseFormat(PropertyNamingStrategy responseFormat) {
		this.responseFormat = responseFormat;
	}

	public String getPathValueDelimiter() {
		return this.pathValueDelimiter;
	}

	public void setPathValueDelimiter(String pathValueDelimiter) {
		this.pathValueDelimiter = pathValueDelimiter;
	}

	public String getPathValuesOpenString() {
		return this.pathValuesOpenString;
	}

	public void setPathValuesOpenString(String pathValuesOpenString) {
		this.pathValuesOpenString = pathValuesOpenString;
	}

	public String getPathValuesCloseString() {
		return this.pathValuesCloseString;
	}

	public void setPathValuesCloseString(String pathValuesCloseString) {
		this.pathValuesCloseString = pathValuesCloseString;
	}

	public boolean isQueued() {
		return this.queued;
	}

	public void setQueued(boolean queued) {
		this.queued = queued;
	}

	public boolean isForcedRequest() {
		return this.forcedRequest;
	}

	public void setForcedRequest(boolean forcedRequest) {
		this.forcedRequest = forcedRequest;
	}

	public String getUsedApiKey() {
		return this.usedApiKey;
	}

	public void setUsedApiKey(String usedApiKey) {
		this.usedApiKey = usedApiKey;
	}

}
