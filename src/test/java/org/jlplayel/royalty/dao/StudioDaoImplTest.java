package org.jlplayel.royalty.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.jlplayel.royalty.config.SpringConfiguration;
import org.jlplayel.royalty.model.Studio;
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
public class StudioDaoImplTest {
    private final static String VALID_STUDIO_ID = "0001a";
    private final static String NO_VALID_STUDIO_ID = "anyID";
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    private StudioDao studioDao;
    
    @Before
    public void initAndPopulateDB(){
        if( studioDao == null ){
            studioDao = new StudioDaoImpl();
            cleanDataBaseInfo();
            ReflectionTestUtils.setField(studioDao, "jdbcTemplate", jdbcTemplate);
            addSomeStudios();
        }
    }
    
    
    @Test
    public void testInsert() {
        
        Studio studio = new Studio();
        studio.setId("0004d");
        studio.setName("TV Show TestInsert");
        studio.setPaymentUnit(new BigDecimal("105.19"));
        studio.setTotalViewing(123);
        
        int recordNum = studioDao.insert(studio);
        
        assertEquals("One insertion was expected.", 1, recordNum);
    }
    
    
    @Test(expected = Exception.class)
    public void testInsert_duplicateStudio() {
        
        Studio studio = new Studio();
        studio.setId(VALID_STUDIO_ID);
        studio.setName("TV Show TestInsert");
        studio.setPaymentUnit(new BigDecimal("1.01"));
        studio.setTotalViewing(0);
        
        studioDao.insert(studio);
    }
    
    
    @Test
    public void testFind_existingRecord() {
        
        List<Studio> studios = studioDao.find(VALID_STUDIO_ID);
        
        assertEquals("One found studio was expected.", 1, studios.size());
        assertEquals("Id should match.", VALID_STUDIO_ID, studios.get(0).getId());
        assertNotNull(studios.get(0).getName());
        assertEquals(new BigDecimal("11.01"), studios.get(0).getPaymentUnit());
    }
    
    
    @Test
    public void testFind_notExistingRecord() {
        
        List<Studio> studio = studioDao.find(NO_VALID_STUDIO_ID);

        assertEquals(0,studio.size());
    }
    
    @Test
    public void testFind_noId(){
        
        List<Studio> studios = studioDao.find(null);
        
        assertNotNull(studios);
        assertEquals(3, studios.size());
    }
    
    
    @Test
    public void testIncreaseOneTotalViewing() {
        
        int updatedRecordNum = studioDao.increaseOneTotalViewing(VALID_STUDIO_ID);
        
        assertEquals("One update was expected.", 1, updatedRecordNum);
    }
    
    
    @Test
    public void testIncreaseOneTotalViewingAndFindStudioPayments(){
        
        List<Studio> studios = studioDao.find(VALID_STUDIO_ID);
        int initialViewings = studios.get(0).getTotalViewing();
        
        studioDao.increaseOneTotalViewing(VALID_STUDIO_ID);
        
        studios = studioDao.find(VALID_STUDIO_ID);
        
        assertEquals(initialViewings + 1, studios.get(0).getTotalViewing());   
    }
    
    
    @Test
    public void testAddStudios(){
        Studio studio1 = new Studio();
        studio1.setId("00010a");
        studio1.setName("Studio 10");
        studio1.setPaymentUnit(new BigDecimal("1.01"));
        
        Studio studio2 = new Studio();
        studio2.setId("00020b");
        studio2.setName("Studio 20");
        studio2.setPaymentUnit(new BigDecimal("2.02"));
        
        
        int recordNum = studioDao.addStudios(Arrays.asList(studio1, studio2));
        
        assertEquals("Two insertions was expected.", 2, recordNum);
    }
    
    
    private void addSomeStudios(){
        
        Studio studio1 = new Studio();
        studio1.setId(VALID_STUDIO_ID);
        studio1.setName("Studio 1");
        studio1.setPaymentUnit(new BigDecimal("11.01"));
        studioDao.insert(studio1);
        
        Studio studio2 = new Studio();
        studio2.setId("0002b");
        studio2.setName("Studio 2");
        studio2.setPaymentUnit(new BigDecimal("22.02"));
        studioDao.insert(studio2);
        
        Studio studio3 = new Studio();
        studio3.setId("0003c");
        studio3.setName("Studio 3");
        studio3.setPaymentUnit(new BigDecimal("33.03"));
        studioDao.insert(studio3);
    }
    
    private void cleanDataBaseInfo(){
        jdbcTemplate.execute("TRUNCATE SCHEMA public AND COMMIT");
    }
}
