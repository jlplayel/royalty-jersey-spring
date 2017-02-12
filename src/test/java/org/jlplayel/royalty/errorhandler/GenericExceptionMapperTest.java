package org.jlplayel.royalty.errorhandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;


public class GenericExceptionMapperTest {
    
    private static final int HTTP_500_INTERNAL_SERVER_ERROR = 
                                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
    
    private static final int HTTP_400_BAD_REQUEST = 
                                    Response.Status.BAD_REQUEST.getStatusCode();
    
    private GenericExceptionMapper gem;
    
    @Before
    public void initiation(){
        if( gem == null ){
            gem = new GenericExceptionMapper();
        }
    }
    
    
    @Test
    public void testToResponse_withNullPointerException(){
        
        Exception exception = new NullPointerException("Testing DuplicateKeyException");
        
        Response response = gem.toResponse(exception);
        
        assertEquals(HTTP_500_INTERNAL_SERVER_ERROR, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        
        ErrorMessage errorMessage = (ErrorMessage) response.getEntity();
        assertEquals(HTTP_500_INTERNAL_SERVER_ERROR, errorMessage.getStatus());
        assertNotNull(errorMessage.getUUID());
        assertTrue("Not valid UUID", !"".equals(errorMessage.getUUID()));
        assertTrue("NullPointerException".equals(errorMessage.getMessage()));
        assertNotNull(errorMessage.getDateTime());
    }
    
    
    @Test
    public void testToResponse_withBadRequestException(){
        
        Exception exception = new BadRequestException("Testing DuplicateKeyException");
        
        Response response = gem.toResponse(exception);
        
        assertEquals(HTTP_400_BAD_REQUEST, response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON, response.getMediaType().toString());
        
        ErrorMessage errorMessage = (ErrorMessage) response.getEntity();
        assertEquals(HTTP_400_BAD_REQUEST, errorMessage.getStatus());
        assertNotNull(errorMessage.getUUID());
        assertTrue("Not valid UUID", !"".equals(errorMessage.getUUID()));
        assertTrue("BadRequestException".equals(errorMessage.getMessage()));
        assertNotNull(errorMessage.getDateTime());
    }

}
