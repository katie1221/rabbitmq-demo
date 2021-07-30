package demo;

import com.rabbitmq.client.*;
import demo.util.ConnectionUtil;

import java.io.IOException;

/**
 * 消费者1：注册成功发送短信服务
 * @author qzz
 */
public class Receive {

    /**
     * 短信队列
     */
    private final static String QUEUE_NAME = "fanout_exchange_queue_sms";
    /**
     * 交换机
     */
    private final static String EXCHANGE_NAME = "test_fanout_exchange";

    public static void main(String[] args) throws Exception{
        //1.获取到连接
        Connection connection = ConnectionUtil.getConnection();
        //2.从连接中创建会话通道，生成者和mq服务所有通信都在channel通道中完成
        Channel channel = connection.createChannel();
        //3.声明队列
        //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        /**
         * 参数说明
         *   1.queue 队列名称
         *   2.durable 是否持久化；如果持久化，mq重启后队列还在
         *   3.exclusive 表示该消息队列是否只在当前connection生效（如果connection 连接关闭，则列队自动删除，如果将此参数设置为true可以用于临时队列的创建）
         *   4.autoDelete 自动删除，队列不再使用时是否自动删除此队列；如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
         *   5.arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
         */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //4.绑定队列到交换机
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME,"");
        //5.定义队列的消费者
        DefaultConsumer consumer = new DefaultConsumer(channel){

            //获取消息，并且处理，这个方法类似事件监听，如果有消息的时候，会被自动调用
            /**
             * 当接受到消息后，此方法将被调用
             * @param consumerTag 消费者标签，用来标识消费者的，在监听队列时设置channel.basicConsume
             * @param envelope 信封，通过envelope
             * @param properties 消息属性
             * @param body  消息内容
             * @throws IOException
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                //body 即消息体
                String msg = new String(body,"utf-8");
                System.out.println("[短信服务] received: " + msg);
            }
        };
        //6.监听队列，第二个参数：是否自动进行消息确认
        //参数：String queue ,boolean autoAck,Consumer callback
        /**
         * 参数说明：
         *   1.queue 队列名称
         *   2.autoAck 自动回复，当消费者接受到消息后要告诉mq消息已接收，如果将此参数设置为true表示会自动回复mq,如果设置为false要通过编程实现回复
         *   3.callback 消费方法，当消费者接收到消息时要执行的方法
         */
        channel.basicConsume(QUEUE_NAME,true,consumer);



    }
}
