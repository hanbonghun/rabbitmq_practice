package io.dodn.demo.messagequeue.config;


import io.dodn.demo.messagequeue.WorkQueueConsumer;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String QUEUE_NAME = "WorkerQueue";
    
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true); // durable이 true이면 서버 껐다 켰을 때 큐 메세지 사라지지 않음
    }
    
    // RabbitMQ와의 메시지 송수신을 단순화하고 관리하는 주요 객체
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }
    
    /**
     * SimpleMessageListenerContainer는 메시지를 수신하는 리스너 컨테이너입니다. 이 객체는 큐에서 메시지를 읽고, 적절한 리스너 메서드를 호출하는 역할을 합니다. 간단히 말하면, 메시지 큐를 계속해서 감시하고, 새로운 메시지가 들어오면
     * 해당 메시지를 처리할 리스너에게 전달하는 역할을 합니다.
     * <p>
     * 주요 역할 큐에 메시지가 도착하면 해당 메시지를 처리할 리스너로 전달. 메시지 수신을 위한 **연결 설정(ConnectionFactory)**과 **큐 설정(Queue Name)**을 관리. 동기/비동기 처리를 위한 리스너가 여러 개 설정 가능.
     * MessageListenerAdapter를 사용해 메시지를 처리할 적합한 메서드로 전달.
     */
    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(listenerAdapter);
        container.setAcknowledgeMode(AcknowledgeMode.AUTO);
        return container;
    }
    
/*
    MessageListenerAdapter는 메시지 리스너가 실제로 메시지를 처리할 메서드를 지정하는 객체입니다. Spring에서는 메시지를 수신할 때, 메시지를 처리할 메서드(일반적으로 receiveMessage)를 호출합니다. 이를 리플렉션(reflection)을 이용해 자동으로 호출할 수 있도록 해주는 역할을 합니다.
    
    주요 역할
    메시지를 받을 메서드를 지정하고, 이 메서드가 실행될 때 메시지를 인자로 넘겨줍니다.
    리스너 객체와 메시지를 처리할 메서드를 연결하는 다리 역할을 합니다.
    Receiver 클래스에서 **receiveMessage**와 같은 메서드를 지정하여, 해당 메서드에서 메시지를 처리하게 합니다.*/
    @Bean
        public MessageListenerAdapter listenerAdapter(WorkQueueConsumer workQueueTask) {
        return new MessageListenerAdapter(workQueueTask, "receiveMessage");
    }
}
