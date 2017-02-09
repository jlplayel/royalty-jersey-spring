package org.jlplayel.royalty.dao;

import java.util.List;

import org.jlplayel.royalty.model.Studio;

public interface StudioDao {
    
    public int insert(Studio studio);
    
    public Studio findBy(String id);
    
    public List<Studio> findAll();
    
    public int increaseOneTotalViewing(String studioId);
    
    public int resetAllStudioVisualizations();
    
    public int addStudios(List<Studio> studios);
}
