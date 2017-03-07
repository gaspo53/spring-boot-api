package com.example.api.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriUtils;

import com.example.api.entities.request.ApiRequest;
import com.example.api.enums.ApiComponent;
import com.example.api.service.ApiBaseUrlResolver;

@Component
public class ApiUrlBuilder {

    @Autowired
    private ApiBaseUrlResolver baseUrlResolver;
    
    
    /**
     * Adds all the {@link ApiRequest#getQueryParameters()} to the returning URL
     * @param apiRequest
     * @param originalMethodUri
     * @return a URL string containing all the parameters in the ApiRequest
     * @throws Exception
     * @throws UnsupportedEncodingException
     */
    public String addParameters(ApiRequest apiRequest, URI originalMethodUri) throws Exception, UnsupportedEncodingException {
        String uriString = originalMethodUri.toString();
        if (apiRequest.isApiContext()){
	        ApiComponent apiComponent = apiRequest.getComponent();
	        uriString = this.addParametersToUrl(apiComponent, uriString, apiRequest.getQueryParameters());
	        uriString = this.joinUriParams(uriString, apiComponent);
        }else{
        	uriString = this.addParametersToUrl(uriString, apiRequest.getQueryParameters());
        }

        String decodedURL = URLDecoder.decode(uriString, Charset.forName("UTF-8").toString());
        return decodedURL;
    }
    
    public String getResourceUrl(ApiRequest apiRequest) {
    	String baseUrl = this.getBaseUrlResolver().getBaseUrl(apiRequest.getComponent());
    	String apiRequestUrl = apiRequest.getUrl();
    	String resourceURL = null;
    	if (apiRequestUrl.length() > baseUrl.length()) {
    		resourceURL = apiRequestUrl.substring(baseUrl.length() + 1);
    	} else {
    		resourceURL = apiRequestUrl;
    	}
    	
    	return resourceURL;
    }
    
    /**
     * Creates a URL containing only the API Base URL
     * @param uri
     * @return
     * @throws Exception
     */
    public String createUrl(ApiComponent component) throws Exception {
    	String baseUrl = this.getBaseUrlResolver().getBaseUrl(component);
        StringBuilder sb = new StringBuilder(baseUrl);
        
        String url = sb.toString();
        String decodedURL = URLDecoder.decode(url,Charset.forName("UTF-8").toString());
        
        return decodedURL;
    }
    
    /**
     * Creates a URL prepending the API Base URL
     * @param uri
     * @return
     * @throws Exception
     */
    public String createUrl(String uri, ApiComponent component) throws Exception {
        StringBuilder sb = new StringBuilder(this.prependBaseApiUrl(uri,component));
        
        String url = sb.toString();
        String decodedURL = URLDecoder.decode(url,Charset.forName("UTF-8").toString());
        
        return decodedURL;
    }

    /**
     * Creates a URL prepending the received string as base path
     * @param base
     * @param url
     * @return
     * @throws Exception
     */
    public String createUrl(String base, String url) throws Exception {
        StringBuilder sb = new StringBuilder(this.prependStringToUrl(base,url));

        String string = sb.toString();
        String decodedURL = URLDecoder.decode(string,Charset.forName("UTF-8").toString());
        
        return decodedURL;
    }
    
    /**
     * Creates a URL prepending the API v3 Base URL, and ads each parameter received to the built URL.
     * @param uri
     * @return
     * @throws Exception
     */
    public String createUrl(ApiComponent component, String uri, List<NameValuePair> params) throws Exception {
        StringBuilder sb = new StringBuilder(this.prependBaseApiUrl(uri, component));
        String url = this.addParametersToUrl(component,uri, params);

        sb.append(url);
        return sb.toString();
    }

    public String addParametersToUrl(ApiComponent component, String uri, List<NameValuePair> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        String url = this.createUrl(uri,component);
        URIBuilder uriBuilder = new URIBuilder(UriUtils.encodeQuery(url,"UTF-8"));
        uriBuilder.addParameters(params);
        sb.append(uriBuilder.build().toASCIIString());

        return sb.toString();
    }

    public String addParametersToUrl(String url, List<NameValuePair> params) throws Exception {
        StringBuilder sb = new StringBuilder();
        URIBuilder uriBuilder = new URIBuilder(UriUtils.encodeQuery(url,"UTF-8"));
        uriBuilder.addParameters(params);
        sb.append(uriBuilder.build().toASCIIString());

        return sb.toString();
    }

    /**
     * Adds a parameter to uri received.
     * @returns a String containing the original uri with the parameter added
     */
    public String addParameterToUrl(ApiComponent component, String uri, String parameterName, String parameterValue) throws Exception {
        StringBuilder sb = new StringBuilder();
        String url = this.createUrl(uri,component);
        URIBuilder uriBuilder = new URIBuilder(UriUtils.encodeQuery(url,"UTF-8"));

        if (StringUtils.isNotEmpty(parameterValue)){
            uriBuilder.addParameter(parameterName,parameterValue);
        }
        sb.append(uriBuilder.build().toASCIIString());
        return sb.toString();
    }

    /**
     * For each value in the list, calls to {@link #addPathValueToUrl(String, String)
     * @param uri
     * @param params
     * @return
     * @throws Exception
     */
    public String addPathValuesToUrl(ApiRequest apiRequest, String uri) throws Exception {
        String finalUri = uri;
        List<String> values = apiRequest.getPathValues();
        Iterator<String> valuesIterator = values.iterator();
        while (valuesIterator.hasNext()){
            String value = valuesIterator.next();
            finalUri = this.addPathValueToUrl(finalUri, value, apiRequest);
        }
        
        return finalUri;
    }

    /**
     * For each value in the list, calls to {@link #addPathValueToUrl(String, String)
     * @param uri
     * @param params
     * @return
     * @throws Exception
     */
    public String addPathValuesToUrl(ApiRequest apiRequest, String uri, List<String> values) throws Exception {
        String finalUri = uri;
        
        Iterator<String> valuesIterator = values.iterator();
        while (valuesIterator.hasNext()){
            String value = valuesIterator.next();
            finalUri = this.addPathValueToUrl(finalUri, value, apiRequest);
        }
        
        return finalUri;
    }

    /**
     * Adds a value to the path of the uri received.
     * @param delimiter defaults to /
     * @returns a String containing the original uri with the parameter added
     */
    public String addPathValueToUrl(String uri, String value, ApiRequest apiRequest) throws Exception {
    	ApiComponent component = apiRequest.getComponent();
    	String delimiter = apiRequest.getPathValueDelimiter();
    	String openString = apiRequest.getPathValuesOpenString();
    	String closeString = apiRequest.getPathValuesCloseString();
    	
        String suffix = StringUtils.defaultString(delimiter, "/");
        
		if (StringUtils.endsWith(uri, suffix)){
            uri = StringUtils.removeEnd(uri, suffix);
        }
        
        StringBuilder sb = new StringBuilder(uri);
        
        if (StringUtils.isNotBlank(openString)){
        	sb.append(openString);
        }
        
        value = StringUtils.removeStart(value, suffix);
        if (StringUtils.isNotEmpty(value)){
            sb.append(suffix).append(value);
        }

        if (StringUtils.isNotBlank(closeString)){
        	sb.append(closeString);
        }
        
        String url = this.createUrl(sb.toString(),component);
        URIBuilder uriBuilder = new URIBuilder(UriUtils.encodeQuery(url,"UTF-8"));
        
        return uriBuilder.build().toASCIIString();
    }

    public String encodeUrlFragment(String s) {
        String result;
        try {
            result = UriUtils.encodeQuery(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }
    
    
    public String joinUriParams(String uri, ApiComponent component){
        String finalURI = null;
        try{
            StringBuffer finalUriSB = new StringBuffer();
            URIBuilder uriBuilder = new URIBuilder(uri);
            Map<String, String> paramsMap = new HashMap<String, String>();
            Iterator<NameValuePair> paramsIterator = uriBuilder.getQueryParams().iterator();
            while (paramsIterator.hasNext()){
                NameValuePair param = paramsIterator.next();
                StringBuffer nameSB = new StringBuffer(param.getName());
                StringBuffer valueSB = new StringBuffer(param.getValue());
                if (!paramsMap.containsKey(nameSB.toString())){
                    paramsMap.put(nameSB.toString(), valueSB.toString());
                }else{
                    StringBuffer valueInMapSB = new StringBuffer(paramsMap.get(nameSB.toString()));
                    valueInMapSB.append(",").append(valueSB);
                    //Replace the key in map
                    paramsMap.put(nameSB.toString(), valueInMapSB.toString());
                }
            }
            finalUriSB.append(uriBuilder.getScheme()).append("://");
            finalUriSB.append(uriBuilder.getHost());
            
            if (uriBuilder.getPort() > -1){
                finalUriSB.append(":").append(uriBuilder.getPort());
            }
            
            finalUriSB.append(uriBuilder.getPath());
            
            finalURI = finalUriSB.toString();
            Iterator<Entry<String, String>> paramsMapIterator = paramsMap.entrySet().iterator();
            while (paramsMapIterator.hasNext()){
                Entry<String, String> entryParam = paramsMapIterator.next();
                finalURI = this.addParameterToUrl(component, finalURI, entryParam.getKey(), entryParam.getValue());
            }
        }catch(Exception e){
            LogHelper.warn(this,e);
            finalURI = uri;
        }
        
        return finalURI;
    }

    /*
     * ************* Private methods
     */

    private String prependBaseApiUrl(String uri, ApiComponent component) throws Exception {
        String baseUrl = this.getBaseUrlResolver().getBaseUrl(component);
        String prependStringToUrl = this.prependStringToUrl(baseUrl, uri);
        
        return prependStringToUrl;
    }

    /**
     * Prepends the base received to the uri.<br>
     * Avoids repeated values by removing all the occurrences of {@link base}
     * @param base
     * @param uri
     * @return
     * @throws Exception
     */
    private String prependStringToUrl(String base, String uri) throws Exception {
        String cleanUri = StringUtils.remove(uri,base);
        
        StringBuilder sb = new StringBuilder(base);
        if (!StringUtils.startsWith(cleanUri, "/")){
            sb.append("/");
        }
        sb.append(cleanUri);
        
        URIBuilder uriBuilder = new URIBuilder(sb.toString());

        return uriBuilder.toString();
    }

    public ApiBaseUrlResolver getBaseUrlResolver() {
        return this.baseUrlResolver;
    }

    public void setBaseUrlResolver(ApiBaseUrlResolver baseUrlResolver) {
        this.baseUrlResolver = baseUrlResolver;
    }
    
}
