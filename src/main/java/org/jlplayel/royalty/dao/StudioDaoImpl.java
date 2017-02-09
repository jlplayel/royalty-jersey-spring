package org.jlplayel.royalty.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jlplayel.royalty.model.Studio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class StudioDaoImpl implements StudioDao{
 
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
        sqlParameters.add(studio.getTotalViewing());
        
        return jdbcTemplate.update(insertSql.toString(), sqlParameters.toArray());
    }
    
    
    @Override
    public Studio findBy(String id){
        
        if( id==null || id.length()==0){
            throw new IllegalArgumentException("StudioID needs a value.");
        }
        
        List<Studio> studios = find(id);
        
        if(studios.size()==0){
            return null;
        }
        
        return find(id).get(0);
    }
    
    
    @Override
    public List<Studio> findAll(){
        return find(null);
    }
    

    private List<Studio> find(String id) {
        
        StringBuilder query = new StringBuilder();  
        query.append("SELECT ID, NAME, PAYMENT_UNIT, TOTAL_VIEWING FROM STUDIO ");
        
        Object[] params = new Object[0];
        
        if(id!=null){
            query.append("WHERE ID = ? ");
            params = new Object[]{id};
        }
        
        List<Studio> studios = jdbcTemplate.query(query.toString(),
                                             params, 
                                             new RowMapper<Studio>() 
                    {
                        public Studio mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Studio studio = new Studio();
                            studio.setId(rs.getString("ID"));
                            studio.setName(rs.getString("NAME"));
                            studio.setPaymentUnit(rs.getBigDecimal("PAYMENT_UNIT"));
                            studio.setTotalViewing(rs.getInt("TOTAL_VIEWING"));
                            return studio;
                        }
                    });
        
        return studios;
    }
    
    
    @Override
    public int increaseOneTotalViewing(String studioId){
        StringBuilder query = new StringBuilder();
        query.append("UPDATE STUDIO SET TOTAL_VIEWING = TOTAL_VIEWING + 1 ");
        query.append("WHERE ID = ? ");
        
        return jdbcTemplate.update(query.toString(), new Object[] { studioId });
    }


    @Override
    public int resetAllStudioVisualizations() {
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
