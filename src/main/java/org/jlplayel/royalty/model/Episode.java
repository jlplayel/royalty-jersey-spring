package org.jlplayel.royalty.model;

import java.util.Objects;

public class Episode {
    
    private String id;
    private String name;
    private String rightsOwner;
    
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
    
    public String getRightsOwner() {
        return rightsOwner;
    }
    
    public void setRightsOwner(String rightsOwner) {
        this.rightsOwner = rightsOwner;
    }
    
    @Override
    public boolean equals(Object object) {

        if (object == this) return true;
        if (!(object instanceof Episode)) {
            return false;
        }
        Episode episode = (Episode) object;
        return Objects.equals(this.id, episode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    
}
