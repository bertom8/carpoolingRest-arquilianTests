package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.LoginRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Stateless
public class LoginRestImpl implements LoginRest {
    private static final Logger logger = LoggerFactory.getLogger(LoginRestImpl.class);

    @Context
    HttpServletRequest request;

    /**
     *
     * @param email email string
     * @param password password string
     * @return Was the login successfully
     */
    @Override
    public boolean login(String email, String password) {
        User user = new UserRestImpl().getUserByEmail(email);
        if (user != null) {
            if (user.getPassword().equals(hash(password))) {
                request.getSession(true).setAttribute("user", user);
                return true;
            }
        } else {
            throw new IllegalArgumentException("There was no user with this data");
        }
        return false;
    }

    /**
     * Log out the user in session
     */
    @Override
    public void logout() {
        request.getSession().removeAttribute("user");
    }

    /**
     *
     * @param password password string
     * @return hash of password string
     */
    static String hash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] buffer = password.getBytes("UTF-8");
            md.update(buffer);
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aDigest : digest) {
                sb.append(Integer.toString((aDigest & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            logger.error("Wrong hash algorithm or encoding");
            return null;
        }
    }
}
