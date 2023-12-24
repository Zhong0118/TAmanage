package com.ood.tamanage.service.impl;

import com.ood.tamanage.enums.Status;
import com.ood.tamanage.mapper.OfferMapper;
import com.ood.tamanage.pojo.Offers;
import com.ood.tamanage.service.OfferService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
    @Resource
    private OfferMapper offerMapper;
    @Override
    public int getOffersCount() {
        return offerMapper.getOffersCount();
    }

    @Override
    public void addOfferByCID(Offers offer, Integer uid) {
        offerMapper.addOfferByCID(offer, uid);
    }

    @Override
    public void addOfferByMID(Offers offer, Integer uid) {
        offerMapper.addOfferByMID(offer, uid);
    }

    @Override
    public List<Offers> getOffersList() {
        return offerMapper.getOffersList();
    }

    @Override
    public List<Offers> getOffersByUid(Integer uid) {
        return offerMapper.getOffersByUid(uid);
    }

    @Override
    public List<Offers> getOffersByUidPending(Integer uid) {
        return offerMapper.getOffersByUidPending(uid);
    }

    @Override
    public void updateOffer(int oid, Status offerStatus) {
        offerMapper.updateOffer(oid, offerStatus);
    }

    @Override
    public List<Offers> getOffersByCID(int cid) {
        return offerMapper.getOffersByCID(cid);
    }

    @Override
    public List<Offers> getOffersByMID(int mid) {
        return offerMapper.getOffersByMID(mid);
    }

    @Override
    public Offers getOfferByOid(int oid) {
        return offerMapper.getOfferByOid(oid);
    }

    @Override
    public void deleteOfferByCourseId(int cid) {
        offerMapper.deleteOfferByCourseId(cid);
    }

    @Override
    public void deleteOfferByModuleId(int mid) {
        offerMapper.deleteOfferByModuleId(mid);
    }

    @Override
    public boolean hasStuedentOfferByCID(int cid, Integer uid) {
        return offerMapper.hasStuedentOfferByCID(cid, uid) > 0;
    }

    @Override
    public boolean hasStuedentOfferByMID(int mid, Integer uid) {
        return offerMapper.hasStuedentOfferByMID(mid, uid) > 0;
    }
}
