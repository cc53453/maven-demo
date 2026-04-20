package io.github.cc53453.datatype.enums;

public enum CompareOperator {
    EQ("="),
    NE("<>"),
    LT("<"),
    LE("<="),
    GT(">"),
    GE(">="),
    ;
    
    private final String symbol;
    
    CompareOperator(String symbol) {
        this.symbol = symbol;
    }
    
    public String getSymbol() {
        return symbol;
    }
}