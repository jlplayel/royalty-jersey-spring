package org.jlplayel.royalty.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.ResourceConfig;
import org.jlplayel.royalty.config.JerseySpringTest;
import org.jlplayel.royalty.errorhandler.GenericExceptionMapper;
import org.jlplayel.royalty.model.CustomerViewing;
import org.jlplayel.royalty.model.Episode;
import org.jlplayel.royalty.model.Studio;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class RoyaltyControllerIntegrationTest extends JerseySpringTest {
    private static final int HTTP_200_OK_STATUS_CODE = Response.Status.OK.getStatusCode();
    private static final int HTTP_202_ACCEPTED_STATUS_CODE = Response.Status.ACCEPTED.getStatusCode();
    private static final int HTTP_404_NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();
    private final int HTTP_500_INTERNAL_SERVER_ERROR = 
                                       Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    
    private static final String VALID_STUDIO_ID = "InitialStudioId001";
    private static final String VALID_EPISODE_ID = "InitialEpisodeId001";
    
    @Override
    protected ResourceConfig configure(){   
        return new ResourceConfig(RoyaltyController.class)
                        .register(GenericExceptionMapper.class);
    }
    
    
    @Before
    public void initiation(){
        addSomeInitialStudios();
        addSomeInitialEpisodes();
    }
    
    
    @Test
    public void testReset() {
        
        addViewingsToEpisode("InitialEpisodeId004", 11);
        
        Response response = target("royaltymanager/reset").request().post(null);
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testViewing() {

        CustomerViewing customerViewing = new CustomerViewing();
        customerViewing.setEpisode(VALID_EPISODE_ID);
        
        Response response = target("royaltymanager/viewing")
                            .request()
                            .post(Entity.entity(customerViewing,MediaType.APPLICATION_JSON));
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testGetAllPayments() {
        
        addViewingsToEpisode("InitialEpisodeId002", 7);
        addViewingsToEpisode("InitialEpisodeId004", 11);
        
        Response response = target("royaltymanager/payments").request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_200_OK_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("royalty\":220.22"));
        assertTrue(responseStr.contains("rightsownerId\":\"InitialStudioId002"));
        assertTrue(responseStr.contains("viewings\":7"));
        assertTrue(responseStr.contains("rightsowner\":\"Initial Studio Name 001"));
    }
    
    
    @Test
    public void testGetPayments_withExistingStudioId() {
        
        addViewingsToEpisode(VALID_EPISODE_ID, 5);
        
        Response response = target("royaltymanager/payments/" + VALID_STUDIO_ID)
                                .request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_200_OK_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("royalty\":5.05"));
        assertTrue(responseStr.contains("viewings\":5"));
        assertTrue(responseStr.contains("rightsowner\":\"Initial Studio Name 001"));
    }
    
    
    @Test
    public void testGetPayments_withoutExistingStudioId() {

        Response response = target("royaltymanager/payments/"+"fakeId").request().get();
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_404_NOT_FOUND, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("GUID not found."));
    }
    
    
    @Test
    public void testAddStudies(){
        
        List<Studio> studios = new ArrayList<>();
        
        Studio studio3 = new Studio();
        studio3.setId("InitialStudioId003");
        studio3.setName("Initial Studio Name 003");
        studio3.setPaymentUnit(new BigDecimal("33.33"));
        studios.add(studio3);
        
        Studio studio4 = new Studio();
        studio4.setId("InitialStudioId004");
        studio4.setName("Initial Studio Name 004");
        studio4.setPaymentUnit(new BigDecimal("4.40"));
        studios.add(studio4);
        
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
        
        Episode episode6 = new Episode();
        episode6.setId("InitialEpisodeId006");
        episode6.setName("Initial Episode Name 006");
        episode6.setRightsOwner("InitialStudioId006");
        episodes.add(episode6);
        
        Episode episode7 = new Episode();
        episode7.setId("InitialEpisodeId007");
        episode7.setName("Initial Episode Name 007");
        episode7.setRightsOwner("InitialStudioId001");
        episodes.add(episode7);
        
        Episode episode8 = new Episode();
        episode8.setId("InitialEpisodeId008");
        episode8.setName("Initial Episode Name 008");
        episode8.setRightsOwner("InitialStudioId003");
        episodes.add(episode8);
        
        Response response = target("royaltymanager/episodes")
                                .request()
                                .post(Entity.entity(episodes,
                                                    MediaType.APPLICATION_JSON));
        
        assertEquals(HTTP_202_ACCEPTED_STATUS_CODE, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertEquals("The body should be empty.", 0, response.getLength());
    }
    
    
    @Test
    public void testGenericException_addingDuplicateStudio(){
        
        List<Studio> studios = new ArrayList<>();
        
        Studio studio1 = new Studio();
        studio1.setId(VALID_STUDIO_ID);
        studio1.setName("Initial Studio Name 001");
        studio1.setPaymentUnit(new BigDecimal("1.01"));
        studios.add(studio1);
        
        Response response = target("royaltymanager/studios").request()
        .post(Entity.entity(studios, MediaType.APPLICATION_JSON));
        
        String responseStr = response.readEntity(String.class);
        
        assertEquals(HTTP_500_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        assertTrue(responseStr.contains("status\":500"));
        assertTrue(responseStr.contains("message\":\"DuplicateKeyException"));
        assertTrue(responseStr.contains("dateTime\":\"20"));
        assertTrue(responseStr.contains("uuid\":\""));
    }
    
    
    private void addSomeInitialStudios(){
        List<Studio> studios = new ArrayList<>();
        
        Studio studio1 = new Studio();
        studio1.setId(VALID_STUDIO_ID);
        studio1.setName("Initial Studio Name 001");
        studio1.setPaymentUnit(new BigDecimal("1.01"));
        studios.add(studio1);
        
        Studio studio2 = new Studio();
        studio2.setId("InitialStudioId002");
        studio2.setName("Initial Studio Name 002");
        studio2.setPaymentUnit(new BigDecimal("20.02"));
        studios.add(studio2);
        
        target("royaltymanager/studios").request()
                      .post(Entity.entity(studios, MediaType.APPLICATION_JSON));
    }
    
    
    private void addSomeInitialEpisodes(){
        List<Episode> episodes = new ArrayList<>();
        
        Episode episode1 = new Episode();
        episode1.setId(VALID_EPISODE_ID);
        episode1.setName("Initial Episode Name 001");
        episode1.setRightsOwner("InitialStudioId001");
        episodes.add(episode1);
        
        Episode episode2 = new Episode();
        episode2.setId("InitialEpisodeId002");
        episode2.setName("Initial Episode Name 002");
        episode2.setRightsOwner("InitialStudioId001");
        episodes.add(episode2);
        
        Episode episode3 = new Episode();
        episode3.setId("InitialEpisodeId003");
        episode3.setName("Initial Episode Name 003");
        episode3.setRightsOwner("InitialStudioId001");
        episodes.add(episode3);
        
        Episode episode4 = new Episode();
        episode4.setId("InitialEpisodeId004");
        episode4.setName("Initial Episode Name 004");
        episode4.setRightsOwner("InitialStudioId002");
        episodes.add(episode4);
        
        Episode episode5 = new Episode();
        episode5.setId("InitialEpisodeId005");
        episode5.setName("Initial Episode Name 005");
        episode5.setRightsOwner("InitialStudioId002");
        episodes.add(episode5);
        
        target("royaltymanager/episodes").request()
                   .post(Entity.entity(episodes, MediaType.APPLICATION_JSON));
    }
    
    
    private void addViewingsToEpisode( String EpisodeId, int viewingNum){
        for(int num = 0; num<viewingNum; num++){
            CustomerViewing customerViewing = new CustomerViewing();
            customerViewing.setEpisode(EpisodeId);
            
            target("royaltymanager/viewing").request()
                   .post(Entity.entity(customerViewing,MediaType.APPLICATION_JSON));
        }
    }
}
