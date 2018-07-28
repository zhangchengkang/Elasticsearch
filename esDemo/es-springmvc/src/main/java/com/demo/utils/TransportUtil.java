package com.demo.utils;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName: TransportUtil
 * @Description: ES工具类
 * @author: ZhangChengKang
 * @date: 2018/7/27
 */
public class TransportUtil {
    @Value("${cluster.name}")
    private static String clusterName;
    @Value("${cluster.address}")
    private static String clusterAddress;
    @Value("${cluster.port}")
    private static String clusterPort;
    private static TransportClient client = null;

    public static TransportClient getConnection() {
        Settings esSettings = Settings.builder().put("clusterName", clusterName).put("client.transport.sniff", true).build();
        client = new PreBuiltTransportClient(esSettings);
        try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(clusterAddress), Integer.valueOf(clusterPort)));
        } catch (UnknownHostException e) {
            client = null;
        }
        return client;
    }

    public static XContentBuilder getMapping(Class eclass) {
        XContentBuilder mapping = null;
        try {
            mapping = XContentFactory.jsonBuilder();
            mapping.startObject();
            mapping.startObject("properties");
            Field[] fields = eclass.getDeclaredFields();
            for (Field field : fields) {
                String value = "text";
                switch (field.getType().getName()) {
                    case "java.util.Date":
                        value = "date";
                        break;
                    case "java.lang.Integer":
                    case "java.lang.Double":
                    case "java.lang.Long":
                    case "java.lang.Boolean":
                    case "java.lang.Float":
                        value = "long";
                        break;
                }
                mapping.startObject(field.getName()).field("type", value).endObject();
            }
            mapping.endObject();
            mapping.endObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapping;
    }
}