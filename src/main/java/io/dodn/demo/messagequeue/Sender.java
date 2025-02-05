package io.dodn.demo.messagequeue;

import io.dodn.demo.messagequeue.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Sender {
    
    private final RabbitTemplate rabbitTemplate;
    
    public void send(String message) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, message);
        System.out.println("Sent <" + message + ">");
    }
    
}
