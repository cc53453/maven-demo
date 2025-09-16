package io.github.cc53453.file.dto;

import java.util.List;

/**
 * 按行读取txt文件转化成的DTO对象需要实现本接口
 */
public interface TxtLineDTO {
    /**
     * 怎么从每一行的String转化为用户自己的DTO
     * @param line 按行读取txt文件得到的list
     */
    public void getDataFromList(List<String> line);
}
