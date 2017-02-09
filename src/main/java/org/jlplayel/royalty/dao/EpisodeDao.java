package org.jlplayel.royalty.dao;

import java.util.List;

import org.jlplayel.royalty.model.Episode;

public interface EpisodeDao {
    
    public int insert( Episode episode );
    
    public Episode find( String id );
    
    public int addEpisodes( List<Episode> episodes );

}
