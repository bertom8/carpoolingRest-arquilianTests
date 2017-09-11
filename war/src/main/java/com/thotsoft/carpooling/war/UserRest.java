package com.thotsoft.carpooling.war;

import com.thotsoft.carpooling.api.model.User;

import javax.naming.NamingException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/user")
public interface UserRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void addUser(User user) throws NamingException;

    @DELETE
    @Path("/{id}")
    boolean removeUser(@PathParam("id") int id) throws NamingException;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    User getUser(@PathParam("id") int id) throws NamingException;

    @POST
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    void updateUser(@PathParam("id") int id, User user) throws NamingException;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<User> listUsers() throws NamingException;
}
