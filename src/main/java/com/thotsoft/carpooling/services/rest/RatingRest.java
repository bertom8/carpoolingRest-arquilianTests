package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Rating;
import com.thotsoft.carpooling.model.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/rating")
public interface RatingRest {
    @PUT
    @Path("/{rate}")
    void rate(User rateWhom, @PathParam("rate") Rating.Rate rate);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    double getRating(User user);
}
