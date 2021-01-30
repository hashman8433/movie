package com.free.movie;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

public class Producer {

    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ProducerGroup");
        producer.setNamesrvAddr("192.168.1.3:9876");
        //producer.set
        producer.start();
        for(int i= 0; i <100; i++){
            Message message = new Message("TBW103", "TagA", ("hello RocketMQ" + i).getBytes("UTF-8"));
            SendResult send = producer.send(message);
            System.out.println(send);
        }
        producer.shutdown();
    }
}
