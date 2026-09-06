package io.github.cc53453.datatype.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;

import org.springframework.util.Assert;

/**
 * 获取列表中某字段最大最小所处index的工具
 * @param <T> 泛型，一般是DTO、Model、POJO等
 */
public class ListMinMaxHelper<T> {
	/** 
    * 原始数据 
    */
   private final List<T> source;
   /**
    * 从哪开始找，包含
    */
   private final Integer from;
   /**
    * 找到哪结束，包含
    */
   private final Integer to;
   
   /**
    * 构造函数
    * @param source 原始数据列表
    * @param from 从哪开始找，包含
    * @param to 找到哪结束，包含
    */
   public ListMinMaxHelper(List<T> source, Integer from, Integer to) {
	   Assert.notNull(source, "source list must not be null");
	   Assert.notEmpty(source, "source list must not be empty");
	   Assert.isTrue(from >= 0 && from < source.size(), "from index out of bounds");
	   Assert.isTrue(to >= 0 && to < source.size(), "to index out of bounds");
	   Assert.isTrue(from <= to, "from index must be less than or equal to to index");
	   this.source = source;
	   this.from = from;
	   this.to = to;
   }
   
   /**
    * 获取列表中某字段(BigDecimal类型)最小值所处的index
    * @param keyExtractor 该字段的get方法
    * @return index
    */
   public int getMinIndex(Function<T, BigDecimal> keyExtractor) {
	   checkValid(keyExtractor);
	   int minIndex = from;
	   BigDecimal min = keyExtractor.apply(source.get(minIndex));
	   for(int i=from + 1;i <= to;i++) {
		   if(min.compareTo(keyExtractor.apply(source.get(i))) > 0) {
			   min = keyExtractor.apply(source.get(i));
			   minIndex = i;
		   }
	   }
	   return minIndex;
   }

   /**
    * 获取列表中某字段(BigDecimal类型)最大值所处的index
    * @param keyExtractor 该字段的get方法
    * @return index
    */
   public int getMaxIndex(Function<T, BigDecimal> keyExtractor) {
	   checkValid(keyExtractor);
	   int maxIndex = from;
	   BigDecimal max = keyExtractor.apply(source.get(maxIndex));
	   for(int i=from + 1;i <= to;i++) {
		   if(max.compareTo(keyExtractor.apply(source.get(i))) < 0) {
			   max = keyExtractor.apply(source.get(i));
			   maxIndex = i;
		   }
	   }
	   return maxIndex;
   }
   
   private void checkValid(Function<T, BigDecimal> keyExtractor) {
	   Assert.notNull(keyExtractor, "keyExtractor must not be null");
   }
}
