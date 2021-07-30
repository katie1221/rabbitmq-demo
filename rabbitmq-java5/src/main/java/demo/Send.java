package demo;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import demo.util.ConnectionUtil;

/**
 * 生产者
 * @author qzz
 */
public class Send {

    private final static String EXCHANGE_NAME = "test_topic_exchange";

    public static void main(String[] args) throws Exception{

        //1.获取到连接
        Connection connection = ConnectionUtil.getConnection();
        //2.从连接中创建会话通道，生成者和mq服务所有通信都在channel通道中完成
        Channel channel = connection.createChannel();
        //3.声明exchange，指定类型为topic
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        //消息内容
        String message = "这是一只行动迅速的橙色的兔子";
        //4.发送消息到Exchange ,并且指定routingkey 为：quick.orange.rabbit
        channel.basicPublish(EXCHANGE_NAME,"quick.orange.rabbit", null,message.getBytes());
        System.out.println("[动物描述：] send '"+ message + "'");

        //5.关闭通道和连接 (资源关闭最好用try-catch-finally 语句处理)
        channel.close();
        connection.close();

    }



}
