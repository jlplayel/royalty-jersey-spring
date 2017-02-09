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
import org.jlplayel.royalty.model.Studio;
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
        
        Studio studio1 = new Studio();
        studio1.setId("test2_rightsOwnerId1");
        studio1.setName("test2_rightsOwner1");
        studio1.setPaymentUnit(new BigDecimal("10.13"));
        studio1.setTotalViewing(7);
        
        Studio studio2 = new Studio();
        studio2.setId("test2_rightsOwnerId2");
        studio2.setName("test2_rightsOwner2");
        studio2.setPaymentUnit(new BigDecimal("1.17"));
        studio2.setTotalViewing(10);
        
        
        Mockito.when( studioDao.findAll() )
               .thenReturn(Arrays.asList(studio1, studio2));
        
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
    public void testGetStudioPayment(){
        
        Studio studio = new Studio();
        studio.setId("test3_rightsOwnerId1");
        studio.setName("test3_rightsOwner1");
        studio.setPaymentUnit(new BigDecimal("1.17"));
        studio.setTotalViewing(100);

        Mockito.when( studioDao.findBy(Mockito.anyString()) )
               .thenReturn(studio);
        
        Payment result = royaltyService.getStudioPayment("test3", false);
        
        assertNull(result.getRightsOwnerId());
        assertTrue( result.getRoyalty().compareTo(new BigDecimal("117.00")) == 0);
        assertNotNull(result.getRightsOwner());
        assertNotNull(result.getViewings());
    }
    
    
    
}
