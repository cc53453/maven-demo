package io.github.cc53453.datatype.util;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * 序列化反序列化工具
 * @param <T> 序列化的对象类型
 */
@Slf4j
public class SerializationHelper<T> {
	private final ObjectMapper objectMapper; 
	
	/**
	 * 构造函数
	 */
    public SerializationHelper() {
        this.objectMapper = new ObjectMapper()
    	        .enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 序列化到文件
     * @param object 对象
     * @param filePath 文件
     * @return 成功为true
     * @throws IOException 写文件失败
     */
    public boolean serializeByJson(T object, String filePath) throws IOException {
    	if(object==null) {
    		throw new IllegalArgumentException("Object to serialize cannot be null");
    	}
    	objectMapper.writeValue(new java.io.File(filePath), object);
    	return true;
    }
    
    /**
     * 传入具体的 Class 类型进行反序列化
     * @param filePath 文件
     * @param typeReference 类型
     * @return 指的类型的对象
     * @throws IOException 反序列化失败
     */
    public T deserializeByJson(String filePath, TypeReference<T> typeReference) throws IOException {
        return objectMapper.readValue(new java.io.File(filePath), typeReference);
    }
    
}
