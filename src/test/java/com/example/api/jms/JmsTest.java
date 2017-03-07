package com.example.api.jms;

import java.io.Serializable;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.api.jms.sender.SenderTest;
import com.example.api.test.base.BaseTest;

public class JmsTest extends BaseTest{

	@Autowired 
	private SenderTest sender;
	
	@Test
	public void sendTextMessageTest(){
		this.sender.sendTextTestMessage();
	}
	
	@Test
	public void sendObjectMessageTest() throws InterruptedException{
		Serializable object = new String("TEST STRING");
		
		this.sender.sendObjectTestMessage(object);
	}
	
}
