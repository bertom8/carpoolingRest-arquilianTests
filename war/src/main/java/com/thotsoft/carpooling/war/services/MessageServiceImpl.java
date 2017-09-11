package com.thotsoft.carpooling.war.services;

import com.thotsoft.carpooling.api.MessageService;
import com.thotsoft.carpooling.api.model.Message;
import com.thotsoft.carpooling.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
@Remote(MessageService.class)
public class MessageServiceImpl implements MessageService {
    private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addMessage(Message message) {

    }

    @Override
    public boolean removeMessage(int id) {
        return false;
    }

    @Override
    public boolean removeMessage(Message message) {
        return false;
    }

    @Override
    public Message getMessage(int id) {
        return null;
    }

    @Override
    public List<Message> listMessages() {
        return null;
    }

    @Override
    public List<Message> listMessagesFrom(User fromUser) {
        return null;
    }

    @Override
    public List<Message> listMessagesTo(User toUser) {
        return null;
    }
}
