package io.github.cc53453.datatype.pojo;

import java.nio.charset.Charset;

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
