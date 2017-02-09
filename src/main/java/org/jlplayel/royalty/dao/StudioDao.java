package org.jlplayel.royalty.dao;

import java.util.List;

import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;

public interface StudioDao {
    
    public int insert(Studio studio);
    
    public Studio find(String id);
    
    public int increaseOneTotalViewing(String studioId);
    
    public Payment findStudioPayments(String studioId);
    
    public List<Payment> getAllStudioPayments();
    
    public int setAllTotalViewingToZero();
    
    public int addStudios(List<Studio> studios);
}
