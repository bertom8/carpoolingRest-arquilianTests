package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Rating;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.RatingRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import java.util.List;

public class RatingRestImpl implements RatingRest {
    private static Logger logger = LoggerFactory.getLogger(RatingRestImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Context
    HttpServletRequest request;

    /**
     *
     * @param rateWhom What user to rate
     * @param rate Rate value
     */
    @Override
    public void rate(User rateWhom, Rating.Rate rate) {
        if (rateWhom == null) {
            throw new IllegalArgumentException("There is no user to rate");
        }
        User user = ((User)request.getSession().getAttribute("user"));
        if (user == null) {
            throw new IllegalArgumentException("No user logged in!");
        }
        Rating rating = new Rating();
        rating.setGiveRating(user);
        rating.setGetRating(rateWhom);
        rating.setRating(rate);
        em.persist(rating);
        em.flush();
        logger.info("Created a new rating: {}", rating);
    }

    /**
     *
     * @param user Optional user parameter, if null it gets user in session
     * @return average rating value of User
     */
    @Override
    public double getRating(User user) {
        if (user == null) {
            user = ((User) request.getSession().getAttribute("user"));
            if (user == null) {
                throw new IllegalArgumentException("No user logged in!");
            }
            return getRatingForUser(user, "giveRating");
        }
        return getRatingForUser(user, "getRating");
    }

    /**
     *
     * @param user User parameter to calculate rating
     * @param whereExpAttribute Expression attrubute,
     *                          if session user, then "giveRating"
     *                          else "getRating"
     * @return average rating value of user
     */
    private double getRatingForUser(User user, String whereExpAttribute) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Rating> criteriaQuery = builder.createQuery(Rating.class);
        Root<Rating> ratingRoot = criteriaQuery.from(Rating.class);
        criteriaQuery.select(ratingRoot);
        criteriaQuery.where(builder.equal(ratingRoot.get(whereExpAttribute), user));
        List<Rating> ratingList = em.createQuery(criteriaQuery).getResultList();
        if (ratingList != null) {
            double rate = 0;
            for (Rating rating : ratingList) {
                rate += rating.getRating().getRate();
            }
            return rate/ratingList.size();
        } else {
            return 0;
        }
    }
}
