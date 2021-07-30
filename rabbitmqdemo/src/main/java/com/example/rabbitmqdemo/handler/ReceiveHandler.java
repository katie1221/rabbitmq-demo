package com.example.rabbitmqdemo.handler;


import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 编写一个监听器组件，通过注解配置消费者队列，以及队列与交换机之间绑定关系（也可以像生产者那样通过配置类配置）、
 * 在SpringAmqp中，对消息的消费者进行了封装和抽象.一个JavaBean的方法，只要添加@RabbitListener注解，就可以成为了一个消费者。
 * @author qzz
 */
@Component
public class ReceiveHandler {

    /**
     * 监听邮件队列
     * @param msg
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value="queue_email",durable = "true"),
                    exchange = @Exchange(
                            value = "topic.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {"topic.#.email.#","email.*"}
            )
    )
    public void receive_email(String msg){
        System.out.println("[邮件服务] received : " + msg + "!");
    }

    /**
     * 监听短信队列
     * @param msg
     */
    @RabbitListener(
            bindings = @QueueBinding(
                    value = @Queue(value="queue_sms",durable = "true"),
                    exchange = @Exchange(
                            value = "topic.exchange",
                            ignoreDeclarationExceptions = "true",
                            type = ExchangeTypes.TOPIC
                    ),
                    key = {"topic.#.sms.#"}
            )
    )
    public void receive_sms(String msg){
        System.out.println("[短信服务] received : " + msg + "!");
    }
}
