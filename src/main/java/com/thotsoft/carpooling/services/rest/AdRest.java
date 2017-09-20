package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Advertisement;
import com.thotsoft.carpooling.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/ad")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface AdRest {
    @PUT
    int addAdvertisement(Advertisement advertisement);

    @DELETE
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeAdvertisement(@PathParam("id") int id);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeAdvertisement(Advertisement advertisement);

    @GET
    @Path("/id")
    @Produces(MediaType.APPLICATION_JSON)
    Advertisement getAdvertisement(@PathParam("id") int id);

    @POST
    @Path("/id")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateAdvertisement(@PathParam("id") int id, Advertisement advertisement);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Advertisement> listAds(User user);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    int countAds(User user);
}
