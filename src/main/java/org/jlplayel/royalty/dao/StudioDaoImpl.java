package org.jlplayel.royalty.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StudioDaoImpl implements StudioDao{
    
    private static final String PAYMENT_SELECT = 
            "SELECT ID, NAME, TOTAL_VIEWING, PAYMENT_UNIT FROM STUDIO ";
 
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @PostConstruct
    private void init() {
        createStudioTable();
    }
    
    
    private void createStudioTable(){
        StringBuilder query = new StringBuilder();
        
        query.append("CREATE TABLE STUDIO ");
        query.append("( ID VARCHAR(35) NOT NULL PRIMARY KEY, ");
        query.append("NAME VARCHAR(35), ");
        query.append("PAYMENT_UNIT DECIMAL(100,2) NOT NULL, ");
        query.append("TOTAL_VIEWING BIGINT NOT NULL )");
        
        jdbcTemplate.execute(query.toString());
    }
    

    @Override
    public int insert(Studio studio) {
        StringBuilder insertSql = new StringBuilder();
        List<Object> sqlParameters = new ArrayList<>();
        
        insertSql.append("INSERT INTO STUDIO (ID, NAME, PAYMENT_UNIT, TOTAL_VIEWING)");
        insertSql.append(" VALUES(?, ?, ?, ?)");
        
        sqlParameters.add(studio.getId());
        sqlParameters.add(studio.getName());
        sqlParameters.add(studio.getPaymentUnit().toString());
        sqlParameters.add(0);
        
        return jdbcTemplate.update(insertSql.toString(), sqlParameters.toArray());
    }
    
    
    @Override
    public Studio find(String id) {
        
        StringBuilder query = new StringBuilder();  
        query.append("SELECT ID, NAME, PAYMENT_UNIT FROM STUDIO ");
        query.append("WHERE ID = ? ");
        
        List<Studio> studios = jdbcTemplate.query(query.toString(),
                                             new Object[] { id }, 
                                             new RowMapper<Studio>() 
                    {
                        public Studio mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Studio studio = new Studio();
                            studio.setId(rs.getString("ID"));
                            studio.setName(rs.getString("NAME"));
                            studio.setPaymentUnit(rs.getBigDecimal("PAYMENT_UNIT"));
                            return studio;
                        }
                    });
        
        if(studios.size()==0){
            return null;
        }
        
        return studios.get(0);
    }
    
    
    @Override
    public int increaseOneTotalViewing(String studioId){
        StringBuilder query = new StringBuilder();
        query.append("UPDATE STUDIO SET TOTAL_VIEWING = TOTAL_VIEWING + 1 ");
        query.append("WHERE ID = ? ");
        
        return jdbcTemplate.update(query.toString(), new Object[] { studioId });
    }
    
    
    @Override
    public Payment findStudioPayments( String studioId ){
        
        StringBuilder query = new StringBuilder();
        query.append(PAYMENT_SELECT);
        query.append("WHERE ID = ? ");
        
        List<Payment> payments = jdbcTemplate.query(query.toString(),
                                                    new Object[] { studioId }, 
                                                    new PaymentRowMapper());
        
        if(payments.size()==0){
            return null;
        }
        
        return payments.get(0);
        
    }
    
    @Override
    public List<Payment> getAllStudioPayments(){
        
        return jdbcTemplate.query(PAYMENT_SELECT, new PaymentRowMapper());
    }


    @Override
    public int setAllTotalViewingToZero() {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE STUDIO SET TOTAL_VIEWING = 0 ");
        
        return jdbcTemplate.update(query.toString());
    }


    @Override
    public int addStudios(List<Studio> studios) {
        int counter = 0;
        for(Studio studio : studios){
            counter = counter + insert(studio);
        }
        return counter;
    }
    
    
}
