package org.jlplayel.royalty.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.jlplayel.royalty.model.Episode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

@Component
public class EpisodeDaoImpl implements EpisodeDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    
    @PostConstruct
    private void init() {
        createEpisodeTable();
    }
    

    @Override
    public int insert(Episode episode) {
        StringBuilder insertSql = new StringBuilder();
        List<Object> sqlParameters = new ArrayList<>();
        
        insertSql.append("INSERT INTO EPISODE (ID, NAME, RIGHT_OWNER)");
        insertSql.append(" VALUES(?, ?, ?)");
        
        sqlParameters.add(episode.getId());
        sqlParameters.add(episode.getName());
        sqlParameters.add(episode.getRightsOwner());
        
        return jdbcTemplate.update(insertSql.toString(), sqlParameters.toArray());
    }
    
    
    @Override
    public Episode find(String id) {
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT ID, NAME, RIGHT_OWNER FROM EPISODE ");
        query.append("WHERE ID = ? ");
        
        List<Episode> episodes = jdbcTemplate.query(query.toString(),
                                             new Object[] { id }, 
                                             new RowMapper<Episode>() 
                    {
                        public Episode mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Episode episode = new Episode();
                            episode.setId(rs.getString("ID"));
                            episode.setName(rs.getString("NAME"));
                            episode.setRightsOwner(rs.getString("RIGHT_OWNER"));
                            return episode;
                        }
                    });
        
        if(episodes.size()==0){
            return null;
        }
        
        return episodes.get(0);
    }
    
    
    private void createEpisodeTable(){
        StringBuilder query = new StringBuilder();
        
        query.append("CREATE TABLE EPISODE ");
        query.append("( ID VARCHAR(35) NOT NULL PRIMARY KEY, ");
        query.append("NAME VARCHAR(35), ");
        query.append("RIGHT_OWNER VARCHAR(35) NOT NULL )");
        
        jdbcTemplate.execute(query.toString());
    }


    @Override
    public int addEpisodes(List<Episode> episodes) {
        int counter = 0;
        for(Episode episode : episodes){
            counter = counter + insert(episode);
        }
        return counter;
    }


}
