package com.thotsoft.carpooling.services.rest;

import com.thotsoft.carpooling.model.Message;
import com.thotsoft.carpooling.model.User;

import javax.ejb.Local;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/message")
public interface MessageRest {
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    void addMessage(Message message);

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeMessage(@PathParam("id") int id);

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    boolean removeMessage(Message message);

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Message getMessage(@PathParam("id") int id);

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    List<Message> listMessages();

    @GET
    @Path("/list/from")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<Message> listMessagesFrom(User fromUser);

    @GET
    @Path("/list/to")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    List<Message> listMessagesTo(User toUser);
}
