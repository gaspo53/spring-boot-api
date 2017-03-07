package com.example.api.jms.sender;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

@Component
public class SenderTest {

    /**
     * Get a copy of the application context
     */
    @Autowired
    private ConfigurableApplicationContext context;

    
    public void sendTextTestMessage(){
        // Send a message
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("ping!");
            }
        };
        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        System.out.println("Sending a new message.");
        jmsTemplate.send("mailbox-destination", messageCreator);    	
    }
    
    public void sendObjectTestMessage(Serializable object){
        // Send a message
        MessageCreator messageCreator = new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(object);
            }
        };
        JmsTemplate jmsTemplate = this.context.getBean(JmsTemplate.class);
        System.out.println("Sending a new message.");
        jmsTemplate.send("itineraries-destination", messageCreator);    	
    }
    
}
