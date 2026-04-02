package io.github.cc53453.datatype.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * 序列化反序列化工具
 */
@Slf4j
public class SerializationHelper<T> {
	private final ObjectMapper objectMapper; 
	
    public SerializationHelper() {
        this.objectMapper = new ObjectMapper()
    	        .enable(SerializationFeature.INDENT_OUTPUT);
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
     * 反序列化
     * @param filePath 文件
     * @return 对象
     * @throws JsonProcessingException 反序列化失败
     */
    public T deserializeByJson(String filePath) throws JsonProcessingException {
    	return objectMapper.readValue(filePath, 
				new TypeReference<T>() {});
	}
}
