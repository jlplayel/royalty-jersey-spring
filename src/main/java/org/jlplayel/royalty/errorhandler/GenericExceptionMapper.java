package org.jlplayel.royalty.errorhandler;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    
    private static Logger logger = Logger.getLogger(GenericExceptionMapper.class.getName());
    
    @Override
    public Response toResponse(Exception exception) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDateTime(LocalDateTime.now().toString());
        setHttpStatus(exception, errorMessage);
        errorMessage.setUUID(UUID.randomUUID().toString());
        errorMessage.setMessage(exception.getClass().getSimpleName());
        
        logger.log(Level.SEVERE, 
                   "Internal error UUID: " + errorMessage.getUUID(),
                   exception);

        return Response.status(errorMessage.getStatus())
                       .entity(errorMessage)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    
    private void setHttpStatus(Exception exception, ErrorMessage errorMessage) {
        if(exception instanceof WebApplicationException) {
            errorMessage.setStatus(((WebApplicationException)exception).getResponse()
                                                                       .getStatus());
        } else {
            errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        }
    }

}
