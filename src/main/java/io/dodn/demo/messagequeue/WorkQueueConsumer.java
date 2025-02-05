package io.dodn.demo.messagequeue;

import org.springframework.stereotype.Component;

@Component
public class WorkQueueConsumer {
    
    public void receiveMessage(String message) {
        String[] messageParts = message.split("\\|");
        String originMessage = messageParts[0];
        int duration = Integer.parseInt(messageParts[1]);
        
        System.out.println("Received <" + originMessage + ">"+ " for " + duration + " seconds");
        
        try{
            System.out.println("now sleeping for " + duration + " seconds");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Done processing <" + originMessage + ">");
    }
    
}
