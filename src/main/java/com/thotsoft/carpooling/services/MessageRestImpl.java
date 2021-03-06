package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Message;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.MessageRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Objects;

@Stateless
public class MessageRestImpl implements MessageRest {
    private static Logger logger = LoggerFactory.getLogger(MessageRestImpl.class);

    @Context
    private HttpServletRequest request;

    @PersistenceContext
    private EntityManager em;

    /**
     * @param message Message object to insert into DB
     * @return Id of Message
     */
    @Override
    public int addMessage(Message message) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        Objects.requireNonNull(message);
        em.persist(message);
        em.flush();
        logger.info("Message added: {}", message);
        return message.getId();
    }

    /**
     * @param id id of Message to remove
     * @return Was this successfully
     */
    @Override
    public boolean removeMessage(int id) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        Message message = getMessage(id);
        if (message != null) {
            if (loggedUser.isAdmin() || loggedUser.equals(message.getTarget())) {
                em.remove(message);
                logger.info("Message was removed with this id: {}", id);
                return true;
            } else {
                throw new IllegalArgumentException("This user can not remove this item: " + loggedUser);
            }
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + id);
        }
    }

    /**
     * @param message Message object to remove
     * @return Was this successfully
     */
    @Override
    public boolean removeMessage(Message message) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        if (message != null) {
            if (loggedUser.isAdmin() || loggedUser.equals(message.getTarget())) {
                em.remove(em.contains(message) ? message : em.merge(message));
                logger.info("Message was removed with this id: {}", message.getId());
                return true;
            } else {
                throw new IllegalArgumentException("This user can not remove this item: " + loggedUser);
            }
        } else {
            throw new IllegalArgumentException("Message was null");
        }
    }

    /**
     * @param id id of Message
     * @return Message object by id
     */
    @Override
    public Message getMessage(int id) {
        return em.find(Message.class, id);
    }

    /**
     * @param id      id of Message
     * @param message Message object by id argument for update to this
     */
    @Override
    public void updateUser(int id, Message message) {
        Objects.requireNonNull(message);
        Message storedMessage = getMessage(id);
        if (storedMessage != null) {
            em.merge(message);
            logger.info("Message updated: {}", message);
        }
    }

    /**
     * @return List of Messages of all
     */
    @Override
    public List<Message> listMessages() {
        return em.createQuery("from Message", Message.class).getResultList();
    }

    /**
     * @param fromUser User object from whom messages came
     * @return List of Messages from argument user
     */
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

    /**
     * @param toUser User object to whom messages went
     * @return List of Messages to argument user
     */
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
