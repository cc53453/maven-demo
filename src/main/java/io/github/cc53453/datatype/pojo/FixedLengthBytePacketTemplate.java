package io.github.cc53453.datatype.pojo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import io.github.cc53453.datatype.util.NumberHelper;

/**
 * 定长byte报文模板
 */
public abstract class FixedLengthBytePacketTemplate {
    /**
     * 常用的补位字符，空格
     */
    protected static final byte CHAR_SPACE = (char)' ';
    /**
     * 常用的补位字符0
     */
    protected static final byte CHAR_ZERO = (char)'0';
    
    /**
     * 调用本方法转化为定长报文
     * @return 定长报文
     */
    public abstract byte[] toByteArray();
    /**
     * 调用本方法读取定长报文
     * @param bytes 定长报文
     */
    public abstract void load(byte[] bytes);

    /**
     * 默认的构造函数
     */
    public FixedLengthBytePacketTemplate() {} // NOSONAR
    
    /**
     * 拼接byte[]
     * @param bas 多个byte[]
     * @return 拼接后的结果
     */
    protected static byte[] concatByteArrays(byte[]... bas) {
        int length = 0;
        for(byte[] ba:bas) {
            if(ba == null) {
                throw new IllegalArgumentException("cannot concat null");
            }
            length += ba.length;
        }
        byte[] result = new byte[length];

        int start = 0;
        for(byte[] ba:bas) {
            System.arraycopy(ba, 0, result, start, ba.length);
            start += ba.length;
        }
        return result;
    }
    
    /**
     * 数字转定长报文,因为数字都是ASCII，默认都用utf8。左补位0. 
     * 比如长度为5，-1补位成-0001；0补位成00000；1补位成00001；-1.1补位成-01.1
     * @param n 数字
     * @param length 补位到多少长度
     * @return 补位后的string转byte[]
     */
    protected static byte[] numberToByteArrayLeftFixedByZero(Number n, int length) {
        byte[] strByte = getBytes(n.toString(), StandardCharsets.UTF_8, length);
        
        byte[] result = new byte[length];
        for(int i=0; i<length - strByte.length; i++) {
            result[i] = CHAR_ZERO;
        }
        
        if(NumberHelper.isNegative(n)) {
            // 负数, 负号提到第一位，后面补0
            result[0] = '-';
            result[length - strByte.length] = CHAR_ZERO;
            System.arraycopy(strByte, 1, result, length - strByte.length + 1, strByte.length - 1);
        }
        else {
            // 正数或0
            System.arraycopy(strByte, 0, result, length - strByte.length, strByte.length);
        }
        return result;
    }
    
    /**
     * 把str转化为length长度的定长byte报文,左补字符
     * @param str 源字符串
     * @param charset 字符集
     * @param length 报文长度
     * @param fixedChar 填空的字符
     * @return 定长报文
     */
    protected static byte[] toByteArrayByLeftFixed(String str, Charset charset, int length, byte fixedChar) {
        byte[] strByte = getBytes(str, charset, length);
        byte[] result = new byte[length];
        System.arraycopy(strByte, 0, result, length - strByte.length, strByte.length);
        
        for(int i=0; i<length - strByte.length; i++) {
            result[i] = fixedChar;
        }
        return result;
    }
    
    /**
     * 把str转化为length长度的定长byte报文,右补字符
     * @param str 源字符串
     * @param charset 字符集
     * @param length 报文长度
     * @param fixedChar 填空的字符
     * @return 定长报文
     */
    protected static byte[] toByteArrayByRightFixed(String str, Charset charset, int length, byte fixedChar) {
        byte[] strByte = getBytes(str, charset, length);
        byte[] result = new byte[length];
        System.arraycopy(strByte, 0, result, 0, strByte.length);
        
        for(int i=strByte.length; i<length; i++) {
            result[i] = fixedChar;
        }
        return result;
    }
    
    private static byte[] getBytes(String str, Charset charset, int length) {
        if(str == null) {
            str = "";
        }
        
        byte[] strByte = str.getBytes(charset);
        if(strByte.length > length) {
            throw new IllegalArgumentException("invalid str because of too long, str: {}".concat(str));
        }
        return strByte;
    }
}
