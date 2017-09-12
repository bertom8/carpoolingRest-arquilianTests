package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/apply")
public interface ApplyRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void apply(Advertisement advertisement) throws IOException;

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    void cancel(Advertisement advertisement) throws IOException;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    List<Advertisement> getAppliedAds();
}
