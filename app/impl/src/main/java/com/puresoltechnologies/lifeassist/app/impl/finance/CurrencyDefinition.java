package com.puresoltechnologies.lifeassist.app.impl.finance;

public class CurrencyDefinition {

    private final String code;
    private final String symbol;
    private final Integer number;
    private final Short e;
    private final String name;

    public CurrencyDefinition(String code, String symbol, Integer number, Short e, String name) {
	super();
	this.code = code;
	this.symbol = symbol;
	this.number = number;
	this.e = e;
	this.name = name;
    }

    public String getCode() {
	return code;
    }

    public String getSymbol() {
	return symbol;
    }

    public Integer getNumber() {
	return number;
    }

    public Short getE() {
	return e;
    }

    public String getName() {
	return name;
    }

    @Override
    public String toString() {
	return code + ": " + name + "(" + symbol + "), num=" + number + ", e=" + e;
    }
}
