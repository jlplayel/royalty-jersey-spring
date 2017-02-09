package org.jlplayel.royalty.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.jlplayel.royalty.dao.EpisodeDao;
import org.jlplayel.royalty.dao.StudioDao;
import org.jlplayel.royalty.model.Payment;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;


public class RoyaltyServiceImplTest {
    
    private RoyaltyService royaltyService;
    private EpisodeDao episodeDao;
    private StudioDao studioDao;
    
    
    @Before
    public void initAndMockClassUnderTest(){
        if( royaltyService == null ){
            royaltyService = new RoyaltyServiceImpl();
            episodeDao = Mockito.mock(EpisodeDao.class);
            ReflectionTestUtils.setField(royaltyService, "episodeDao", episodeDao);
            studioDao = Mockito.mock(StudioDao.class);
            ReflectionTestUtils.setField(royaltyService, "studioDao", studioDao);
        }
        else{
            Mockito.reset(episodeDao);
            Mockito.reset(studioDao);
        }
    }
    
    
    
    @Test(expected=Exception.class)
    public void testIncreaseOneTheViewing_noValidEpisodeId(){
        
        Mockito.when( episodeDao.find(Mockito.anyString()) )
               .thenReturn(null);
        
        royaltyService.increaseOneTheViewing("test1Id");
    }
    
    
    @Test
    public void testGetAllStudioPayments(){
        
        Payment payment1 = new Payment();
        payment1.setRightsOwnerId("test2_rightsOwnerId1");
        payment1.setRightsOwner("test2_rightsOwner1");
        payment1.setViewings(7);
        payment1.setPaymentUnit(new BigDecimal("10.13"));
        
        Payment payment2 = new Payment();
        payment2.setRightsOwnerId("test2_rightsOwnerId2");
        payment2.setRightsOwner("test2_rightsOwner2");
        payment2.setViewings(10);
        payment2.setPaymentUnit(new BigDecimal("1.17"));
        
        
        
        Mockito.when( studioDao.getAllStudioPayments() )
               .thenReturn(Arrays.asList(payment1, payment2));
        
        List<Payment> payments = royaltyService.getAllStudioPayments();
        
        assertEquals("Ther should be 2 payments.", 2, payments.size());
        assertEquals("test2_rightsOwnerId1", payments.get(0).getRightsOwnerId());
        assertEquals("test2_rightsOwnerId2", payments.get(1).getRightsOwnerId());
        assertTrue( payments.get(0).getRoyalty().compareTo(new BigDecimal("70.91")) == 0);
        assertTrue( payments.get(1).getRoyalty().compareTo(new BigDecimal("11.70")) == 0);
        assertNotNull(payments.get(0).getRightsOwner());
        assertNotNull(payments.get(1).getRightsOwner());
        assertNotNull(payments.get(0).getViewings());
        assertNotNull(payments.get(1).getViewings());
    }
    
    
    @Test
    public void testGetPaymentsWithoutRightsOwnerId(){
        
        Payment payment = new Payment();
        payment.setRightsOwnerId("test3_rightsOwnerId1");
        payment.setRightsOwner("test3_rightsOwner1");
        payment.setViewings(100);
        payment.setPaymentUnit(new BigDecimal("1.17"));
        
        
        
        Mockito.when( studioDao.findStudioPayments(Mockito.anyString()) )
               .thenReturn(payment);
        
        Payment result = royaltyService.getPaymentsWithoutRightsOwnerId("test3");
        
        assertNull(result.getRightsOwnerId());
        assertTrue( payment.getRoyalty().compareTo(new BigDecimal("117.00")) == 0);
        assertNotNull(payment.getRightsOwner());
        assertNotNull(payment.getViewings());
    }
    
    
    
}
