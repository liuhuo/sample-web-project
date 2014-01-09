package org.liuhuo.spring.model;

public class IspInfo {
    private int id;
    private String ispIp;
    private String ispName;

    public IspInfo(int id, String ip, String name) {
        this.id = id;
        this.ispIp = ip;
        this.ispName = name;
    }
    
    public int getId() { return this.id; }
    public String getIspIp() { return this.ispIp; }
    public String getIspName() { return this.ispName; }

    @Override
    public String toString() {
        return "<"+this.id + " "  + this.ispIp + " " + this.ispName+">";
    }
    
}
