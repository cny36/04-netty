package com.example.netty.netty.encode.protobuf;

import com.example.netty.netty.encode.java.Messager;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class ProtobufUtils {

    private static Map<Class<?>, Schema<?>> schemaMap = new ConcurrentHashMap<>();

    /**
     * 序列化，实现将对象转换为字节数组
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serializer(T obj) {
        Class<T> clazz = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {

            Schema<T> schema = getSchema(clazz);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    public static <T> T deserializer(byte[] data, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            Schema<T> schema = getSchema(clazz);
            ProtostuffIOUtil.mergeFrom(data, obj, schema);
            return obj;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) throws InterruptedException {
        Schema<T> schema = (Schema<T>) schemaMap.get(clazz);
        if (schema == null) {
            schema = RuntimeSchema.getSchema(clazz);
            log.info("create schema:{}", schema);
            if (schema != null) {
                schemaMap.put(clazz, schema);
            }
        }
        return schema;
    }

    public static void main(String[] args) {
        Messager messager = new Messager("rrrr", new Date());
        byte[] serializer = ProtobufUtils.serializer(messager);
        log.info("serializer.length = {}", serializer.length);

        Messager deserializer = ProtobufUtils.deserializer(serializer, Messager.class);
        log.info("反序列化后obj : {}", deserializer);
    }
}
