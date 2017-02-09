package org.jlplayel.royalty.service;

import java.util.List;

import org.jlplayel.royalty.model.Episode;
import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;

public interface RoyaltyService {
    
    public void increaseOneTheViewing(String episodeId);
    
    public List<Payment> getAllStudioPayments();
    
    public Payment getStudioPayment(String studioId, boolean withStudioId);
    
    public void resetAllTotalViewings();
    
    public void addStudios( List<Studio> studios );
    
    public void addEpisodes( List<Episode> episodes );
    
}
