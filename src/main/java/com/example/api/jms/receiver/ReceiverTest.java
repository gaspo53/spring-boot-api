package com.example.api.jms.receiver;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class ReceiverTest {

    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "mailbox-destination", containerFactory = "myJmsContainerFactory")
    public void receiveTextMessage(String message) {
        System.out.println("Received <" + message + ">");
    }
    
    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "itineraries-destination", containerFactory = "myJmsContainerFactory")
    public void receiveObjectMessage(Object flightItineraryResponse) {
        System.out.println("Received <" + flightItineraryResponse + ">");
    }
    
}
