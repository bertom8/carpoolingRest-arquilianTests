package com.thotsoft.carpooling.services;

import com.thotsoft.carpooling.model.Advertisement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

public class AdRestImpl implements AdRest {
    private static Logger logger = LoggerFactory.getLogger(AdRestImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public void addAdvertisement(Advertisement advertisement) {
        Objects.requireNonNull(advertisement);
        em.persist(advertisement);
        em.flush();
        logger.info("Advertisement added: {}", advertisement);
    }

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

    @Override
    public Advertisement getAdvertisement(int id) {
        return em.find(Advertisement.class, id);
    }

    @Override
    public void updateAdvertisement(int id, Advertisement advertisement) {
        Objects.requireNonNull(advertisement);
        Advertisement ad = getAdvertisement(id);
        if (ad != null) {
            em.merge(advertisement);
            logger.info("Advertisement updated: {}", advertisement);
        }
    }

    @Override
    public List<Advertisement> listAds() {
        return em.createQuery("from Advertisement", Advertisement.class).getResultList();
    }
}
