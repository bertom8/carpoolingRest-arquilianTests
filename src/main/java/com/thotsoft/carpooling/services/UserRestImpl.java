package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

@Stateless
public class UserRestImpl implements UserRest {
    private static final Logger logger = LoggerFactory.getLogger(UserRestImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addUser(User user) throws NamingException {
        Objects.requireNonNull(user);
        em.persist(user);
        em.flush();
        logger.info("User added: {}", user);
    }

    @Override
    public boolean removeUser(int id) throws NamingException {
        User user = getUser(id);
        if (user != null) {
            em.remove(user);
            logger.info("User was removed with this id: {}", id);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + id);
        }
    }

    @Override
    public boolean removeUser(User user) {
        if (user != null) {
            em.remove(user);
            logger.info("User was removed with this id: {}", user.getId());
            return true;
        } else {
            throw new IllegalArgumentException("User was null");
        }
    }

    @Override
    public User getUser(int id) throws NamingException {
        return em.find(User.class, id);
    }

    @Override
    public void updateUser(int id, User user) throws NamingException {
        Objects.requireNonNull(user);
        User storedUser = getUser(id);
        if (storedUser != null) {
            em.merge(user);
            logger.info("User updated: {}", user);
        }
    }

    @Override
    public List<User> listUsers() throws NamingException {
        return em.createQuery("from User", User.class).getResultList();
    }

    @Override
    public boolean isAlreadyRegistered(String email) {
        Objects.requireNonNull(email);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
        criteriaQuery.where(builder.equal(userRoot.get(User.FIELD_EMAIL), email));
        return !em.createQuery(criteriaQuery).getResultList().isEmpty();
    }
}
