package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Advertisement;
import com.thotsoft.carpooling.model.Apply;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.ApplyRest;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Stateless
public class ApplyRestImpl implements ApplyRest {
    private static Logger logger = LoggerFactory.getLogger(ApplyRestImpl.class);
    @Context
    HttpServletRequest request;
    @PersistenceContext
    private EntityManager em;

    /**
     * @param advertisement Advertisement object to apply by session user
     * @throws IOException throws if Advertisement is full
     */
    @Override
    public void apply(Advertisement advertisement) throws IOException {
        Objects.requireNonNull(advertisement);
        User user = ((User) request.getSession().getAttribute("user"));
        if (user == null) {
            throw new IllegalArgumentException("No user logged in!");
        }
        advertisement = em.find(Advertisement.class, advertisement.getId());
        if (advertisement.getNumOfSeats() - countAppliesForAdvertisement(advertisement) > 0) {
            Advertisement storedAd = em.find(Advertisement.class, advertisement.getId());
            if (storedAd.isSeeking()) {
                throw new IllegalArgumentException("This is an seeking advertisement, you can not apply for that");
            }
            Apply apply = new Apply();
            apply.setUser(user);
            apply.setAdvertisement(storedAd);
            apply.setDate(new Date());
            em.persist(apply);
            em.flush();
            logger.info("Created a new apply: {}", apply);
        } else {
            throw new IOException("No more empty seat");
        }
    }

    /**
     * @param advertisement Advertisement object to cancel by session user
     * @throws IOException throws if this advertisement is not applied for session user
     */
    @Override
    public void cancel(Advertisement advertisement) throws IOException {
        Objects.requireNonNull(advertisement);
        User user = ((User) request.getSession().getAttribute("user"));
        if (user == null) {
            throw new IllegalArgumentException("No user logged in!");
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Apply> criteriaQuery = builder.createQuery(Apply.class);
        Root<Apply> applyRoot = criteriaQuery.from(Apply.class);
        criteriaQuery.select(applyRoot);
        criteriaQuery.where(builder.and(builder.equal(applyRoot.get("user"), user)),
                builder.equal(applyRoot.get("advertisement"), advertisement));
        List<Apply> applyList = em.createQuery(criteriaQuery).getResultList();
        if (applyList != null) {
            em.remove(applyList.get(0));
            logger.info("Removed an apply: {}", applyList.get(0));
        } else {
            throw new IOException("There was no apply for this advertisement and logged user");
        }
    }

    /**
     * @return list of applied advertisement of session user
     */
    @Override
    public List<Advertisement> getAppliedAds() {
        User user = ((User) request.getSession().getAttribute("user"));
        if (user == null) {
            throw new IllegalArgumentException("No user logged in!");
        }
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Apply> criteriaQuery = builder.createQuery(Apply.class);
        Root<Apply> applyRoot = criteriaQuery.from(Apply.class);
        criteriaQuery.select(applyRoot);
        criteriaQuery.where(builder.equal(applyRoot.get("user"), user));
        List<Apply> applyList = em.createQuery(criteriaQuery).getResultList();
        if (applyList != null) {
            List<Advertisement> ads = new ArrayList<>();
            applyList.forEach(apply -> {
                ads.add(apply.getAdvertisement());
            });
            return ads;
        }
        return null;
    }

    /**
     * @param advertisement Advertisement object
     * @return count of user applies for advertisement
     */
    private int countAppliesForAdvertisement(Advertisement advertisement) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Apply> criteriaQuery = builder.createQuery(Apply.class);
        Root<Apply> applyRoot = criteriaQuery.from(Apply.class);
        criteriaQuery.select(applyRoot);
        criteriaQuery.where(builder.equal(applyRoot.get("advertisement"), advertisement));
        List<Apply> applyList = em.createQuery(criteriaQuery).getResultList();
        if (applyList != null) {
            return applyList.size();
        } else {
            return 0;
        }
    }
}
