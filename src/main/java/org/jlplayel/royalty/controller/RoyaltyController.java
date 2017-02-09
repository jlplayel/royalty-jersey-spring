package org.jlplayel.royalty.controller;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jlplayel.royalty.model.CustomerViewing;
import org.jlplayel.royalty.model.Episode;
import org.jlplayel.royalty.model.Payment;
import org.jlplayel.royalty.model.Studio;
import org.jlplayel.royalty.service.RoyaltyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Path("/royaltymanager") 
@Controller
public class RoyaltyController {
    
    @Autowired
    private RoyaltyService royaltyService;
    
    @POST
    @Path("/reset")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(){
        
        royaltyService.resetAllTotalViewings();
        
        return Response.accepted("").build();
    }
    
    
    @POST
    @Path("/viewing")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response viewing(CustomerViewing customerViewing){
        
        royaltyService.increaseOneTheViewing(customerViewing.getEpisode());
        
        return Response.accepted("").build();
    }
    
    
    @GET
    @Path("/payments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Payment> getAllPayments(){
        
        return royaltyService.getAllStudioPayments();
    }
    
    
    @GET
    @Path("/payments/{ownerID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPayments( @PathParam("ownerID") String ownerID ){
        
        Payment payment = royaltyService.getPaymentsWithoutRightsOwnerId(ownerID);
        
        if( payment==null ){
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("GUID not found.").build();
        }
            
        return Response.status(Response.Status.OK).entity(payment).build();
    }
    
    @POST
    @Path("/studios")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addStudios(List<Studio> studios){
        
        royaltyService.addStudios( studios );
        
        return Response.accepted("").build();
    }
    
    
    @POST
    @Path("/episodes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addEpisodes(List<Episode> episodes){
        
        royaltyService.addEpisodes( episodes );
        
        return Response.accepted("").build();
    }

}
