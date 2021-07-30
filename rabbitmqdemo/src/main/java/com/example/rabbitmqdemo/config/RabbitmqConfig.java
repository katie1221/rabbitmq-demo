package com.example.rabbitmqdemo.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitmqConfig配置类，配置Exchange、Queue、以及绑定交换机
 * @author qzz
 */
@Configuration
public class RabbitmqConfig {

    /**
     * email队列
     */
    public static final String QUEUE_EMAIL = "queue_email";
    /**
     * sms队列
     */
    public static final String QUEUE_SMS = "queue_sms";

    /**
     * topics类型交换机
     */
    public static final String EXCHANGE_NAME = "topic.exchange";

    /**
     *  email指定的routingkey
     *     *(星号) 用来表示一个单词 (必须出现的)
     *     #(井号) 用来表示任意数量（零个或多个）单词
     */
    public static final String ROUTINGKEY_EMAIL = "topic.#.email.#";

    /**
     * sms指定的routingkey
     */
    public static final String ROUTINGKEY_SMS = "topic.#.sms.#";

    /**
     * 声明交换机
     * @return
     */
    @Bean(EXCHANGE_NAME)
    public Exchange exchange(){
        //durable(true)持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_NAME).durable(true).build();
    }
    /**
     * 声明email队列
     * new Queue(QUEUE_EMAIL,true,false,false)
     * durable="true"持久化---rabbitmq重启的时候不需要创建新的队列，默认true
     * auto-delete 表示消息队列没有在使用时将被自动删除，默认为false。
     * exclusive 表示该消息队列是否只在当前connection生效，默认是false。
     * @return
     */
    @Bean(QUEUE_EMAIL)
    public Queue emailQueue(){
        return new Queue(QUEUE_EMAIL);
    }

    /**
     * 声明sms队列
     * @return
     */
    @Bean(QUEUE_SMS)
    public Queue smsQueue(){
        return new Queue(QUEUE_SMS);
    }

    /**
     * ROUTINGKEY_EMAIL 队列绑定到交换机，指定routingKey
     * @param queue 队列
     * @param exchange 交换机
     * @return
     */
    @Bean
    public Binding bindingEmail(@Qualifier(QUEUE_EMAIL)Queue queue, @Qualifier(EXCHANGE_NAME) Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }

    /**
     * ROUTINGKEY_SMS 队列绑定到交换机，指定routingKey
     * @param queue 队列
     * @param exchange 交换机
     * @return
     */
    @Bean
    public Binding bindingSMS(@Qualifier(QUEUE_SMS)Queue queue, @Qualifier(EXCHANGE_NAME) Exchange exchange){

        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }

}
