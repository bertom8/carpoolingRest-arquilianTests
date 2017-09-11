package com.thotsoft.carpooling.war;

import com.thotsoft.carpooling.api.UserService;
import com.thotsoft.carpooling.api.model.User;
import com.thotsoft.carpooling.war.services.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.NamingException;
import java.util.List;
import java.util.Objects;

public class UserRestImpl implements UserRest {
    private static final Logger logger = LoggerFactory.getLogger(UserRestImpl.class);

    @Override
    public void addUser(User user) throws NamingException {
        Objects.requireNonNull(user);
        try (ConnectServer<UserService> connection = connect()) {
            connection.connect().addUser(user);
        } catch (NamingException e) {
            throw new NamingException(e.getExplanation());
        }
    }

    @Override
    public boolean removeUser(int id) throws NamingException {
        try (ConnectServer<UserService> connection = connect()) {
            return connection.connect().removeUser(id);
        } catch (NamingException e) {
            throw new NamingException(e.getExplanation());
        }
    }

    @Override
    public User getUser(int id) throws NamingException {
        try (ConnectServer<UserService> connection = connect()) {
            return connection.connect().getUser(id);
        } catch (NamingException e) {
            throw new NamingException(e.getExplanation());
        }
    }

    @Override
    public void updateUser(int id, User user) throws NamingException {
        Objects.requireNonNull(user);
        try (ConnectServer<UserService> connection = connect()) {
            connection.connect().updateUser(id, user);
        } catch (NamingException e) {
            throw new NamingException(e.getExplanation());
        }
    }

    @Override
    public List<User> listUsers() throws NamingException {
        try (ConnectServer<UserService> connection = connect()) {
            return connection.connect().listUsers();
        } catch (NamingException e) {
            throw new NamingException(e.getExplanation());
        }
    }

    private ConnectServer<UserService> connect() {
        return new ConnectServer<>("localhost", 8080, UserServiceImpl.class.getSimpleName());
    }
}
