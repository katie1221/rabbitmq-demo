package com.example.rabbitmqdemo;

import com.example.rabbitmqdemo.config.RabbitmqConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
class RabbitmqdemoApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 为了方便测试，直接把生产者代码放工程测试类:
     *    发送routing key是"topic.sms.email"的消息，那么mq-rabbitmq-consumer下那些监听的（与交换机(topic.exchange)绑定，
     *    并且订阅的routingkey中匹配了"topic.sms.email"规则的） 队列就会收到消息
     */

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    public void sendMsgByTopics(){

        for(int i=0;i<5;i++){
            String message="恭喜您，注册成功！userid="+i;
            rabbitTemplate.convertAndSend(RabbitmqConfig.EXCHANGE_NAME,"topic.sms.email",message);
            System.out.println("[x] Sent '"+message+"'");
        }
    }
}
