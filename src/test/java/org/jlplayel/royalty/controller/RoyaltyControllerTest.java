package org.jlplayel.royalty.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.jlplayel.royalty.config.JerseySpringTest;
import org.jlplayel.royalty.model.CustomerViewing;
import org.jlplayel.royalty.model.Episode;
import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;
import org.jlplayel.royalty.service.RoyaltyService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
public class RoyaltyControllerTest extends JerseySpringTest{
    
    private final int HTTP_200_OK_STATUS_CODE = Response.Status.OK.getStatusCode();
    private final int HTTP_202_ACCEPTED_STATUS_CODE = Response.Status.ACCEPTED.getStatusCode();
    private final int HTTP_404_NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();
    
    private RoyaltyController royaltyController;
    private RoyaltyService royaltyService;
    
    @Override
    protected ResourceConfig configure() {
        royaltyController = new RoyaltyController();
        return new ResourceConfig().register(royaltyController);
    }
    
    
    @Before
    public void initiation(){
        if( royaltyService == null ){
            royaltyService = Mockito.mock(RoyaltyService.class);
            ReflectionTestUtils.setField(royaltyController, "royaltyService", royaltyService);
        }
        else{
            Mockito.reset(royaltyService);
        }
    }
    
    
    @Test
    public void testReset() {
        
        Mockito.doNothing().when(royaltyService).resetAllTotalViewings();
        
        Response response = target("royaltymanager/reset").request().post(null);
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testViewing() {
        
        Mockito.doNothing().when(royaltyService).increaseOneTheViewing(Mockito.anyString());

        CustomerViewing customerViewing = new CustomerViewing();
        customerViewing.setEpisode("any");
        
        Response response = target("royaltymanager/viewing")
                            .request()
                            .post(Entity.entity(customerViewing,MediaType.APPLICATION_JSON));
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testAllPayments() {
        
        Payment payment1 = new Payment();
        payment1.setRightsOwnerId("235lwemf-234r");
        payment1.setRightsOwner("Name");
        payment1.setRoyalty( new BigDecimal("15000.23") );
        payment1.setViewings(123);
        
        Payment payment2 = new Payment();
        payment2.setRightsOwnerId("adfwer335-24afad");
        payment2.setRightsOwner("Name2");
        payment2.setRoyalty( new BigDecimal("1230.32") );
        payment2.setViewings(432);
        
        Mockito.when( royaltyService.getAllStudioPayments() )
               .thenReturn( Arrays.asList(payment1, payment2) );
        
        
        Response response = target("royaltymanager/payments").request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_200_OK_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("royalty\":15000.23"));
        assertTrue(responseStr.contains("rightsownerId\":\"235lwemf-234r"));
        assertTrue(responseStr.contains("viewings\":432"));
        assertTrue(responseStr.contains("rightsowner\":\"Name2"));
    }
    
    
    @Test
    public void testGetPayments_withExistingStudioId() {
        
        Payment payment = new Payment();
        payment.setRightsOwner("Name");
        payment.setRoyalty( new BigDecimal("15000.23") );
        payment.setViewings(123);
        
        Mockito.when( royaltyService.getStudioPayment(Mockito.anyString(), Mockito.anyBoolean()) )
               .thenReturn(payment);
        
        
        Response response = target("royaltymanager/payments/"+"12443sd").request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_200_OK_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("royalty\":15000.23"));
        assertTrue(responseStr.contains("viewings\":123"));
        assertTrue(responseStr.contains("rightsowner\":\"Name"));
    }
    
    
    @Test
    public void testGetPayments_withoutExistingStudioId() {
        
        Mockito.when( royaltyService.getStudioPayment(Mockito.anyString(), Mockito.anyBoolean()) )
               .thenReturn(null);
        
        
        Response response = target("royaltymanager/payments/"+"12443sd").request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_404_NOT_FOUND, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("GUID not found."));
    }
    
    
    @Test
    public void testAddStudies(){
        
        List<Studio> studios = new ArrayList<>();
        
        Studio studio1 = new Studio();
        studio1.setId("test01id");
        studio1.setName("Test Name1");
        studio1.setPaymentUnit(new BigDecimal("11.11"));
        studios.add(studio1);
        
        Studio studio2 = new Studio();
        studio2.setId("test02id");
        studio2.setName("Test Name2");
        studio2.setPaymentUnit(new BigDecimal("22.22"));
        studios.add(studio2);
        
        Mockito.doNothing().when(royaltyService).addStudios(Mockito.anyList());
        
        Response response = target("royaltymanager/studios")
                                .request()
                                .post(Entity.entity(studios,
                                                    MediaType.APPLICATION_JSON));
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testAddEpisodes(){
        
        List<Episode> episodes = new ArrayList<>();
        
        Episode episode1 = new Episode();
        episode1.setId("test01id");
        episode1.setName("Test Name1");
        episode1.setRightsOwner("Studio1");
        episodes.add(episode1);
        
        Episode episode2 = new Episode();
        episode2.setId("test02id");
        episode2.setName("Test Name2");
        episode2.setRightsOwner("Studio2");
        episodes.add(episode2);
        
        Mockito.doNothing().when(royaltyService).addEpisodes(Mockito.anyList());
        
        Response response = target("royaltymanager/episodes")
                                .request()
                                .post(Entity.entity(episodes,
                                                    MediaType.APPLICATION_JSON));
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }

}
