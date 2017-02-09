package org.jlplayel.royalty.dao;

import java.util.List;

import org.jlplayel.royalty.model.Studio;

public interface StudioDao {
    
    public int insert(Studio studio);
    
    public List<Studio> find(String id);
    
    public int increaseOneTotalViewing(String studioId);
    
    public int setAllTotalViewingToZero();
    
    public int addStudios(List<Studio> studios);
}
