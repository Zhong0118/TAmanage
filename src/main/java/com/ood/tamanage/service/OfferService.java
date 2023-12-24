package com.ood.tamanage.service;

import com.ood.tamanage.enums.Status;
import com.ood.tamanage.pojo.Offers;

import java.util.List;

public interface OfferService {
    int getOffersCount();

    void addOfferByCID(Offers offer, Integer uid);
    void addOfferByMID(Offers offer, Integer uid);

    List<Offers> getOffersList();

    List<Offers> getOffersByUid(Integer uid);

    List<Offers> getOffersByUidPending(Integer uid);

    void updateOffer(int oid, Status offerStatus);

    List<Offers> getOffersByCID(int cid);
    List<Offers> getOffersByMID(int mid);

    Offers getOfferByOid(int oid);

    void deleteOfferByCourseId(int cid);

    void deleteOfferByModuleId(int mid);

    boolean hasStuedentOfferByCID(int cid, Integer uid);

    boolean hasStuedentOfferByMID(int mid, Integer uid);
}
