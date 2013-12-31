package org.liuhuo.spring.model;

public class Tmp {
    private String str;

    public Tmp() {}

    public Tmp(String str) {
        this.str = str;
    }

    public String getStr() {
        return this.str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return "<"+str+">";
    }
}
