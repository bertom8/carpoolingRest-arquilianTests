package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Rating;
import com.thotsoft.carpooling.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/rating")
public interface RatingRest {
    @PUT
    @Path("/{rate}")
    void rate(User rateWhom, @PathParam("rate") Rating.Rate rate);

    @GET
    double getRating(User user);
}
