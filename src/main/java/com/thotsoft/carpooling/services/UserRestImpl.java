package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.UserRest;
import org.apache.commons.validator.routines.EmailValidator;
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

    @Context
    HttpServletRequest request;

    @PersistenceContext
    private EntityManager em;

    /**
     * @param user User object to insert into DB
     */
    @Override
    public int addUser(User user) {
        Objects.requireNonNull(user);
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("Not valid email");
        }
        user.setPassword(LoginRestImpl.hash(user.getPassword()));
        em.persist(user);
        em.flush();
        logger.info("User added: {}", user);
        return user.getId();
    }

    /**
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
     * @param id id of User
     * @return User object by id argument
     */
    @Override
    public User getUser(int id) {
        return em.find(User.class, id);
    }

    /**
     * @param id   id of User for update
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
     * @return List of Users of all
     */
    @Override
    public List<User> listUsers() {
        return em.createQuery("from User", User.class).getResultList();
    }

    /**
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
     * @return count of Users
     */
    @Override
    public int countUsers() {
        return listUsers().size();
    }

    /**
     * @param email email string
     * @return User object by email
     */
    public User getUserByEmail(String email) {
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
