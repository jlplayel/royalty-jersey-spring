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
        
        List<Payment> payments = studioDao.getAllStudioPayments();
        
        payments = addTotalRoyaltyAmount( payments );
 
        return payments;
    }


    @Override
    public Payment getPaymentsWithoutRightsOwnerId(String studioId) {
        Payment payment = studioDao.findStudioPayments(studioId);
        
        if( payment!=null ){
            payment.setRightsOwnerId(null);
            payment = addTotalRoyaltyAmount(payment);
        }
        
        return payment;
    }
    
    
    private List<Payment> addTotalRoyaltyAmount( List<Payment> payments ){
        
        payments.parallelStream()
                .map( p -> addTotalRoyaltyAmount(p)).collect(Collectors.toList());
        
        return payments;
    }
    
    
    private Payment addTotalRoyaltyAmount( Payment payment ){
        
        BigDecimal royalty = payment.getPaymentUnit()
                                    .multiply(BigDecimal.valueOf(payment.getViewings()));
        
        payment.setRoyalty(royalty);
        
        return payment;
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
