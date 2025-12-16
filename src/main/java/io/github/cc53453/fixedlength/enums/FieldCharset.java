package io.github.cc53453.fixedlength.enums;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import lombok.Getter;

@Getter
public enum FieldCharset {
    UTF_8(StandardCharsets.UTF_8),
    ISO_8859_1(StandardCharsets.ISO_8859_1),
    GBK(Charset.forName("GBK"));

    private final Charset charset;

    FieldCharset(Charset charset) {
        this.charset = charset;
    }
}
