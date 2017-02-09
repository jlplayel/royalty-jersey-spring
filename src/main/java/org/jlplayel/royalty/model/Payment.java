package org.jlplayel.royalty.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(Include.NON_EMPTY)
public class Payment {
    
    @JsonProperty("rightsownerId")
    private String rightsOwnerId;
    
    @JsonProperty("rightsowner")
    private String rightsOwner;
    
    private BigDecimal royalty;
    private int viewings;
    
    
    public String getRightsOwnerId() {
        return rightsOwnerId;
    }
    
    public void setRightsOwnerId(String rightsOwnerId) {
        this.rightsOwnerId = rightsOwnerId;
    }
    
    public String getRightsOwner() {
        return rightsOwner;
    }
    
    public void setRightsOwner(String rightsOwner) {
        this.rightsOwner = rightsOwner;
    }
    
    public BigDecimal getRoyalty() {
        return royalty;
    }
    
    public void setRoyalty(BigDecimal royalty) {
        this.royalty = royalty;
    }
    
    public int getViewings() {
        return viewings;
    }
    
    public void setViewings(int viewings) {
        this.viewings = viewings;
    }
    
}
