package io.github.cc53453.datatype.util;

import org.bouncycastle.util.Arrays;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * byte[]游标读取助手
 */
@Slf4j
public class ByteArrayReader {
    private byte[] array;
    @Setter
    private Integer pos;
    
    public ByteArrayReader(byte[] array) {
        this.array = Arrays.copyOf(array, array.length);
        pos = 0;
    }
    
    public byte[] next(Integer length) {
        byte[] result = new byte[length];
        if(pos + length > array.length) {
            throw new ArrayIndexOutOfBoundsException(
                    "array.length=" + array.length + ", pos=" + pos + ", length=" + length);
        }
        log.debug("read from {} to {}", pos, pos + length);
        System.arraycopy(array, pos, result, 0, length);
        pos += length;
        return result;
    }
}
