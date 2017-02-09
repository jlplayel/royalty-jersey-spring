package org.jlplayel.royalty.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.jlplayel.royalty.dao.EpisodeDao;
import org.jlplayel.royalty.dao.StudioDao;
import org.jlplayel.royalty.model.Episode;
import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoyaltyServiceImpl implements RoyaltyService {
    
    @Autowired
    private EpisodeDao episodeDao;
    
    @Autowired
    private StudioDao studioDao;

    @Override
    public void increaseOneTheViewing(String episodeId) {
        
        Episode episode = episodeDao.find(episodeId);
        
        if( episode == null ){
            throw new IllegalArgumentException("Episode ID doesn't exist: " + episodeId);
        }
        
        studioDao.increaseOneTotalViewing(episode.getRightsOwner());
    }
    
    
    @Override
    public List<Payment> getAllStudioPayments(){
        
        List<Studio> studios = studioDao.find(null);
 
        return getAllPayments( studios );
    }


    @Override
    public Payment getPaymentsWithoutRightsOwnerId(String studioId) {
        
        List<Studio> studios = studioDao.find(studioId);
        
        if(studios.size()==0){
            return null;
        }
            
        Payment payment = getTotalPaymentOf( studios.get(0) );
        
        if( payment!=null ){
            payment.setRightsOwnerId(null);
        }
        
        return payment;
    }
    
    
    private List<Payment> getAllPayments( List<Studio> studios ){
        
        List<Payment> payments = studios.parallelStream()
               .map( s -> getTotalPaymentOf(s)).collect(Collectors.toList());
        
        return payments;
    }
    
    
    private Payment getTotalPaymentOf( Studio studio ){
        
        BigDecimal royalty = studio.getPaymentUnit()
                                   .multiply(BigDecimal.valueOf(studio.getTotalViewing()));
        
        Payment result = new Payment();
        result.setRightsOwnerId(studio.getId());
        result.setRightsOwner(studio.getName());
        result.setViewings(studio.getTotalViewing());
        result.setRoyalty(royalty);
        
        return result;
    }


    @Override
    public void resetAllTotalViewings() {
        studioDao.setAllTotalViewingToZero();
    }


    @Override
    public void addStudios(List<Studio> studios) {
        studioDao.addStudios(studios);
    }


    @Override
    public void addEpisodes(List<Episode> episodes) {
        episodeDao.addEpisodes( episodes );
    }
    

}
