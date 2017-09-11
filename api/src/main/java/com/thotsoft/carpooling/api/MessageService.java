package com.thotsoft.carpooling.api;

import com.thotsoft.carpooling.api.model.Message;
import com.thotsoft.carpooling.api.model.User;

import java.util.List;

public interface MessageService {
    void addMessage(Message message);

    boolean removeMessage(int id);

    boolean removeMessage(Message message);

    Message getMessage(int id);

    List<Message> listMessages();

    List<Message> listMessagesFrom(User fromUser);

    List<Message> listMessagesTo(User toUser);
}
