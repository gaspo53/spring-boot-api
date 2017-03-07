package com.example.api.common.utils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.apache.http.client.methods.HttpRequestBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.api.connector.ApiConnector;
import com.example.api.entities.ApiCall;
import com.example.api.entities.request.ApiRequest;
import com.example.api.enums.ApiComponent;

/**
 * Queues requests to be executed in a future.
 * @author Gaspar Rajoy <a href="mailto:gaspo53@gmail.com">gaspo53@gmail.com</a>
 *
 */

@Component
public class RequestsManager {

	@Autowired
	private ApiCommonUtils apiCommonUtils;
	
	@Autowired
	private ApiConnector apiConnector;
	
	@Value("${app.request_manager.scheduler.enabled:true}")
	private boolean schedulerEnabled;
	
	private Queue<ApiCall> queue;
	
	private Map<ApiComponent, LocalDateTime> providerStatus;

	private Map<ApiComponent, Set<String>> bannedApiKeys;
	
	
	@PostConstruct
	private void initProviderStatusAndRequestsQueue(){
		this.queue = new ConcurrentLinkedQueue<>();
		this.bannedApiKeys = new HashMap<>();
		this.providerStatus = new HashMap<>();
		
		List<ApiComponent> components = Arrays.asList(ApiComponent.values());
		components.forEach(component -> {
			this.providerStatus.put(component, LocalDateTime.now());
			this.bannedApiKeys.put(component, new HashSet<>());
		});
	}
	
	@Scheduled(fixedDelay=30000)
	public void processQueue(){
		if (this.schedulerEnabled){
			if (!this.getQueue().isEmpty()){
				LogHelper.warn("Entering to process requests QUEUE, with "+this.getQueue().size()+" request to make");
				this.getQueue().forEach(apiCall -> {
					try{
						if (this.isProviderUnlocked(apiCall.getApiRequest().getComponent())){
							this.getQueue().remove();
							this.getApiConnector().apiCall(apiCall.getApiRequest(), apiCall.getRequestMethod());
						}
					}catch(Exception e){
						LogHelper.error(this,e);
					}
				});
			}
		}
	}
	
	public void queueRequest(ApiRequest apiRequest, HttpRequestBase httpMethod){
		ApiComponent component = apiRequest.getComponent();
		
		//Mark the apiKey as banned
		String usedApiKey = apiRequest.getUsedApiKey();
		this.bannedApiKeys.get(component).add(usedApiKey);
		
		//If the component is not cacheable, no need to queue the request
		if (this.getApiCommonUtils().isCacheable(component)){
			apiRequest.setQueued(true);
			ApiCall apiCall = new ApiCall(apiRequest,httpMethod);
			this.getQueue().add(apiCall);
		}

		//If someone wanted to enqueue a request, it means that the provider has respond with 403/429
		if (this.isProviderUnlocked(component)){
			this.lockProvider(component);
		}
	}
	
	public boolean isProviderUnlocked(ApiComponent apiComponent){
		boolean isProviderUnlocked = false;
		
		try{
			LocalDateTime availabilityDate = this.providerStatus.get(apiComponent);
			isProviderUnlocked = LocalDateTime.now().isAfter(availabilityDate);
			if (isProviderUnlocked){
				//If the provider is unlocked, have to clear the banned api keys
				this.bannedApiKeys.get(apiComponent).clear();
			}
		}catch(Exception e){
			LogHelper.error(this,e);
		}
		
		return isProviderUnlocked;
	}
	
	public boolean isProviderUnlocked(ApiRequest apiRequest){
		boolean isProviderUnlocked = false;
		
		try{
			ApiComponent component = apiRequest.getComponent();
			String usedApiKey = apiRequest.getUsedApiKey();
			
			boolean isApiKeyBanned = this.bannedApiKeys.get(component).contains(usedApiKey);
			
			isProviderUnlocked = (!isApiKeyBanned) || (this.isProviderUnlocked(component));
		}catch(Exception e){
			LogHelper.error(this,e);
		}
		
		return isProviderUnlocked;
	}
	
	
	//Private methods
	
	private void lockProvider(ApiComponent apiComponent){
		LocalDateTime resetTime = LocalDateTime.now();

		Integer thresholdReset = this.getApiCommonUtils().thresholdReset(apiComponent);
		if (thresholdReset > 0){
			resetTime = resetTime.plusMinutes(thresholdReset);
		}
		
		this.providerStatus.put(apiComponent,resetTime);
	}
	
	
	//Getters and setters
	public ApiCommonUtils getApiCommonUtils() {
		return this.apiCommonUtils;
	}

	public void setApiCommonUtils(ApiCommonUtils apiCommonUtils) {
		this.apiCommonUtils = apiCommonUtils;
	}

	public ApiConnector getApiConnector() {
		return this.apiConnector;
	}

	public void setApiConnector(ApiConnector apiConnector) {
		this.apiConnector = apiConnector;
	}

	public Queue<ApiCall> getQueue() {
		return this.queue;
	}

	public void setQueue(Queue<ApiCall> queue) {
		this.queue = queue;
	}
	
}
