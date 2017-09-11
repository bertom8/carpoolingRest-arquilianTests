package com.thotsoft.carpooling.api;

import com.thotsoft.carpooling.api.model.Advertisement;

import java.util.List;

public interface AdService extends CarpoolingService {

    void addAdvertisement(Advertisement advertisement);

    boolean removeAdvertisement(int id);

    boolean removeAdvertisement(Advertisement advertisement);

    Advertisement getAdvertisement(int id);

    void updateAdvertisement(int id, Advertisement advertisement);

    List<Advertisement> listAds();
}
