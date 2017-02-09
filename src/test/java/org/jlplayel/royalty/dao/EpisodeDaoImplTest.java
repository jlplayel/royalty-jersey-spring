package org.jlplayel.royalty.dao;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.jlplayel.royalty.config.SpringConfiguration;
import org.jlplayel.royalty.model.Episode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfiguration.class)
public class EpisodeDaoImplTest {
    
    private final static String VALID_EPISODE_ID = "0001a";
    private final static String NO_VALID_EPISODE_ID = "anyID";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private EpisodeDao episodeDao;
    
    @Before
    public void initAndPopulateDB(){
        if( episodeDao == null ){
            episodeDao = new EpisodeDaoImpl();
            cleanDataBaseInfo();
            ReflectionTestUtils.setField(episodeDao, "jdbcTemplate", jdbcTemplate);
            addSomeEpisodes();
        }
    }
    
    
    @Test
    public void testInsert() {
        
        Episode episode = new Episode();
        episode.setId("0004d");
        episode.setName("TV Show TestInsert");
        episode.setRightsOwner("100oa");
        
        int recordNum = episodeDao.insert(episode);
        
        assertEquals("One insertion was expected.", 1, recordNum);
    }
    
    
    @Test(expected = Exception.class)
    public void testInsert_withExistingSamePreviosId() {
        
        Episode episode = new Episode();
        episode.setId(VALID_EPISODE_ID);
        episode.setName("TV Show TestInsert");
        episode.setRightsOwner("100oa");
        
        episodeDao.insert(episode);
    }
    
    
    @Test
    public void testFind_existingRecord() {
        
        Episode episode = episodeDao.find(VALID_EPISODE_ID);
        
        assertEquals("One insertion was expected.", VALID_EPISODE_ID, episode.getId());
        assertNotNull(episode.getName());
        assertNotNull(episode.getRightsOwner());
    }
    
    
    @Test
    public void testFind_notExistingRecord() {
        
        Episode episode = episodeDao.find(NO_VALID_EPISODE_ID);

        assertNull(episode);
    }
    
    @Test
    public void testAddStudios(){
        Episode episode1 = new Episode();
        episode1.setId("00010a");
        episode1.setName("House 1");
        episode1.setRightsOwner("100oa");
        
        Episode episode2 = new Episode();
        episode2.setId("00020b");
        episode2.setName("Prison Break 2");
        episode2.setRightsOwner("100oa");
        
        
        int recordNum = episodeDao.addEpisodes(Arrays.asList(episode1, episode2));
        
        assertEquals("Two insertions was expected.", 2, recordNum);
    }
    
    
    private void addSomeEpisodes(){
        
        Episode episode1 = new Episode();
        episode1.setId(VALID_EPISODE_ID);
        episode1.setName("House 1");
        episode1.setRightsOwner("100oa");
        episodeDao.insert(episode1);
        
        Episode episode2 = new Episode();
        episode2.setId("0002b");
        episode2.setName("Prison Break 2");
        episode2.setRightsOwner("100oa");
        episodeDao.insert(episode2);
        
        Episode episode3 = new Episode();
        episode3.setId("0003c");
        episode3.setName("Start War 3");
        episode3.setRightsOwner("200ob");
        episodeDao.insert(episode3);
    }
    
    private void cleanDataBaseInfo(){
        jdbcTemplate.execute("TRUNCATE SCHEMA public AND COMMIT");
    }
}
