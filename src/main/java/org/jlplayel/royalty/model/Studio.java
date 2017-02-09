package org.jlplayel.royalty.model;

import java.math.BigDecimal;


public class Studio {
    
    private String id;
    private String name;
    private BigDecimal paymentUnit;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public BigDecimal getPaymentUnit() {
        return paymentUnit;
    }
    
    public void setPaymentUnit(BigDecimal paymentUnit) {
        this.paymentUnit = paymentUnit;
    }
    
    
}
