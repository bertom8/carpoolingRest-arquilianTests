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
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

@Stateless
@Remote(MessageService.class)
public class MessageServiceImpl implements MessageService {
    private static Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addMessage(Message message) {
        Objects.requireNonNull(message);
        em.persist(message);
        em.flush();
        logger.info("Message added: {}", message);
    }

    @Override
    public boolean removeMessage(int id) {
        Message message = getMessage(id);
        if (message != null) {
            em.remove(message);
            logger.info("Message was removed with this id: {}", id);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + id);
        }
    }

    @Override
    public boolean removeMessage(Message message) {
        if (message != null) {
            em.remove(message);
            logger.info("Message was removed with this id: {}", message.getId());
            return true;
        } else {
            throw new IllegalArgumentException("Message was null");
        }
    }

    @Override
    public Message getMessage(int id) {
        return em.find(Message.class, id);
    }

    @Override
    public List<Message> listMessages() {
        return em.createQuery("from Message", Message.class).getResultList();
    }

    @Override
    public List<Message> listMessagesFrom(User fromUser) {
        Objects.requireNonNull(fromUser);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Message> criteriaQuery = builder.createQuery(Message.class);
        Root<Message> messageRoot = criteriaQuery.from(Message.class);
        criteriaQuery.select(messageRoot);
        criteriaQuery.where(builder.equal(messageRoot.get(Message.FIELD_SOURCE), fromUser));
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Message> listMessagesTo(User toUser) {
        Objects.requireNonNull(toUser);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Message> criteriaQuery = builder.createQuery(Message.class);
        Root<Message> messageRoot = criteriaQuery.from(Message.class);
        criteriaQuery.select(messageRoot);
        criteriaQuery.where(builder.equal(messageRoot.get(Message.FIELD_TARGET), toUser));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
