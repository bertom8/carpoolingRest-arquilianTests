package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.UserRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;
import javax.ws.rs.core.Context;
import java.util.List;
import java.util.Objects;

@Stateless
public class UserRestImpl implements UserRest {
    private static final Logger logger = LoggerFactory.getLogger(UserRestImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Context
    HttpServletRequest request;

    /**
     *
     * @param user User object to insert into DB
     */
    @Override
    public void addUser(User user) {
        Objects.requireNonNull(user);
        if (!user.getEmail().matches("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])" +
                "*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4]" +
                "[0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:" +
                "(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\" +
                "\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")) {
            throw new ValidationException("Not valid email");
        }
        user.setPassword(LoginRestImpl.hash(user.getPassword()));
        em.persist(user);
        em.flush();
        logger.info("User added: {}", user);
    }

    /**
     *
     * @param id id of user
     * @return Was this successfully
     */
    @Override
    public boolean removeUser(int id) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        User user = getUser(id);
        if (user != null && loggedUser.isAdmin()) {
            em.remove(user);
            logger.info("User was removed with this id: {}", id);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + id);
        }
    }

    /**
     *
     * @param user User object to remove from DB
     * @return Was this successfully
     */
    @Override
    public boolean removeUser(User user) {
        User loggedUser = ((User) request.getSession().getAttribute("user"));
        if (loggedUser == null) {
            throw new IllegalArgumentException("No user logged in!");
        }

        if (user != null && loggedUser.isAdmin()) {
            em.remove(user);
            logger.info("User was removed with this id: {}", user.getId());
            return true;
        } else {
            throw new IllegalArgumentException("User was null");
        }
    }

    /**
     *
     * @param id id of User
     * @return User object by id argument
     */
    @Override
    public User getUser(int id) {
        return em.find(User.class, id);
    }

    /**
     *
     * @param id id of User for update
     * @param user User object by id argument for update to this
     */
    @Override
    public void updateUser(int id, User user) {
        Objects.requireNonNull(user);
        User storedUser = getUser(id);
        if (storedUser != null) {
            em.merge(user);
            logger.info("User updated: {}", user);
        }
    }

    /**
     *
     * @return List of Users of all
     */
    @Override
    public List<User> listUsers() {
        return em.createQuery("from User", User.class).getResultList();
    }

    /**
     *
     * @param email email string
     * @return Exists user with this email
     */
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

    /**
     *
     * @param user
     * @return count of Users
     */
    //TODO: is really needed to give User argument????
    @Override
    public int countUsers(User user) {
        if (user == null) {
            return listUsers().size();
        } else {
            CriteriaBuilder builder = em.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
            Root<User> userRoot = criteriaQuery.from(User.class);
            criteriaQuery.select(userRoot);
            criteriaQuery.where(builder.equal(userRoot.get(User.FIELD_ID), user.getId()));
            List<User> list = em.createQuery(criteriaQuery).getResultList();
            if (!list.isEmpty()) {
                return list.size();
            } else {
                return 0;
            }
        }
    }

    /**
     *
     * @param email email string
     * @return User object by email
     */
    User getUserByEmail(String email) {
        Objects.requireNonNull(email);
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = builder.createQuery(User.class);
        Root<User> userRoot = criteriaQuery.from(User.class);
        criteriaQuery.select(userRoot);
        criteriaQuery.where(builder.equal(userRoot.get(User.FIELD_EMAIL), email));
        List<User> list = em.createQuery(criteriaQuery).getResultList();
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
}
