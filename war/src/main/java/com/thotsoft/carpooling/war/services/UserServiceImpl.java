package com.thotsoft.carpooling.war.services;

import com.thotsoft.carpooling.api.UserService;
import com.thotsoft.carpooling.api.model.Advertisement;
import com.thotsoft.carpooling.api.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Stateless
@Remote(UserService.class)
public class UserServiceImpl implements UserService {
    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addUser(User user) {
        Objects.requireNonNull(user);
        em.persist(user);
        em.flush();
        logger.info("User added: {}", user);
    }

    @Override
    public boolean removeUser(int id) {
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
    public User getUser(int id) {
        return em.find(User.class, id);
    }

    @Override
    public void updateUser(int id, User user) {
        Objects.requireNonNull(user);
        User storedUser = getUser(id);
        if (storedUser != null) {
            em.merge(user);
        }
    }

    @Override
    public List<User> listUsers() {
        return em.createQuery("from User", User.class).getResultList();
    }

    @Override
    public boolean isAlreadyRegistered(String email) {
        //TODO: implement this
        return false;
    }
}
