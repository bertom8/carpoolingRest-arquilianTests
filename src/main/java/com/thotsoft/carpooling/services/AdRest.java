package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Advertisement;

import javax.ejb.Local;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/ad")
public interface AdRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void addAdvertisement(Advertisement advertisement);

    @DELETE
    @Path("/{id}")
    boolean removeAdvertisement(@PathParam("id") int id);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    boolean removeAdvertisement(Advertisement advertisement);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Advertisement getAdvertisement(@PathParam("id") int id);

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAdvertisement(@PathParam("id") int id, Advertisement advertisement);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Advertisement> listAds();
}
