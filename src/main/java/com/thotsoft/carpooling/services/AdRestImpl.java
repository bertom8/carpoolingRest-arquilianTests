package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Advertisement;
import com.thotsoft.carpooling.model.User;
import com.thotsoft.carpooling.services.rest.AdRest;
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
public class AdRestImpl implements AdRest {
    private static Logger logger = LoggerFactory.getLogger(AdRestImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Context
    HttpServletRequest request;

    /**
     *
     * @param advertisement Advertisement object to insert to DB
     */
    @Override
    public void addAdvertisement(Advertisement advertisement) {
        Objects.requireNonNull(advertisement);
        em.persist(advertisement);
        em.flush();
        logger.info("Advertisement added: {}", advertisement);
    }

    /**
     *
     * @param id id of Advertisement to remove from DB
     * @return Was this successfully
     */
    @Override
    public boolean removeAdvertisement(int id) {
        Advertisement ad = getAdvertisement(id);
        if (ad != null) {
            em.remove(ad);
            logger.info("Advertisement was removed with this id: {}", id);
            return true;
        } else {
            throw new IllegalArgumentException("There was no such item in database: " + id);
        }
    }

    /**
     *
     * @param advertisement Advertosement object to remove from DB
     * @return Was this successfully
     */
    @Override
    public boolean removeAdvertisement(Advertisement advertisement) {
        if (advertisement != null) {
            em.remove(advertisement);
            logger.info("Advertisement removed: {}", advertisement);
            return true;
        } else {
            throw new IllegalArgumentException("Advertisement was null");
        }
    }

    /**
     *
     * @param id id of Advertisement to get from DB
     * @return Advertisement object by id argument
     */
    @Override
    public Advertisement getAdvertisement(int id) {
        return em.find(Advertisement.class, id);
    }

    /**
     *
     * @param id id of Advertisement to update
     * @param advertisement Advertisement object for update operation with id field equals id argument
     */
    @Override
    public void updateAdvertisement(int id, Advertisement advertisement) {
        Objects.requireNonNull(advertisement);
        Advertisement ad = getAdvertisement(id);
        if (ad != null) {
            em.merge(advertisement);
            logger.info("Advertisement updated: {}", advertisement);
        }
    }

    /**
     *
     * @param user Optional User object to list its Advertisements
     * @return List of Advertisement of argument user or session user
     */
    @Override
    public List<Advertisement> listAds(User user) {
        if (user == null) {
            return em.createQuery("from Advertisement", Advertisement.class).getResultList();
        } else {
            return getAdvertisementsByUser(user);
        }
    }

//    @Override
//    public List<Advertisement> listAds(AdOptions options) {
//        return null;
//    }

    /**
     *
     * @param user Optional User object to count its Advertisements
     * @return count of advertisement of argument user or session user
     */
    @Override
    public int countAds(User user) {
        if (user == null) {
            return listAds(null).size();
        } else {
            List<Advertisement> list = getAdvertisementsByUser(user);
            if (!list.isEmpty()) {
                return list.size();
            } else {
                return 0;
            }
        }
    }

    /**
     *
     * @param user User object
     * @return List of Advertisement of user
     */
    private List<Advertisement> getAdvertisementsByUser(User user) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Advertisement> criteriaQuery = builder.createQuery(Advertisement.class);
        Root<Advertisement> adRoot = criteriaQuery.from(Advertisement.class);
        criteriaQuery.select(adRoot);
        criteriaQuery.where(builder.equal(adRoot.get(User.FIELD_ID), user.getId()));
        return em.createQuery(criteriaQuery).getResultList();
    }
}
