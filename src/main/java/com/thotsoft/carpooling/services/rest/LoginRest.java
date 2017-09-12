package com.thotsoft.carpooling.services.rest;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/login")
public interface LoginRest {

    @POST
    boolean login(@FormParam("email") String email, @FormParam("password") String password);

    void logout();
}
