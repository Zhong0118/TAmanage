package com.ood.tamanage.mapper;

import com.ood.tamanage.enums.Status;
import com.ood.tamanage.pojo.Offers;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface OfferMapper {

    @Select("SELECT COUNT(*) FROM offers")
    int getOffersCount();

    @Insert("INSERT INTO offers VALUES (null, #{uid}, #{offer.cid}, null, #{offer.status}, #{offer.description})")
    void addOfferByCID(@Param("offer") Offers offer, @Param("uid") Integer uid);

    @Insert("INSERT INTO offers VALUES (null, #{uid},  null, #{offer.mid}, #{offer.status}, #{offer.description})")
    void addOfferByMID(@Param("offer") Offers offer, @Param("uid") Integer uid);

    @Select("SELECT * FROM offers")
    List<Offers> getOffersList();

    @Select("SELECT * FROM offers WHERE uid = #{uid}")
    List<Offers> getOffersByUid(Integer uid);

    @Select("SELECT * FROM offers WHERE uid = #{uid} AND status = 'Pending'")
    List<Offers> getOffersByUidPending(Integer uid);

    @Update("UPDATE offers SET status = #{offerStatus} WHERE oid = #{oid}")
    void updateOffer(@Param("oid") int oid, @Param("offerStatus") Status offerStatus);

    @Select("SELECT * FROM offers WHERE cid = #{cid}")
    List<Offers> getOffersByCID(int cid);

    @Select("SELECT * FROM offers WHERE mid = #{mid}")
    List<Offers> getOffersByMID(int mid);

    @Select("SELECT * FROM offers WHERE oid = #{oid}")
    Offers getOfferByOid(int oid);

    @Delete("DELETE FROM offers WHERE cid = #{cid}")
    void deleteOfferByCourseId(int cid);

    @Delete("DELETE FROM offers WHERE mid = #{mid}")
    void deleteOfferByModuleId(int mid);

    @Select("SELECT COUNT(*) FROM offers WHERE uid = #{uid} AND cid = #{cid} ")
    int hasStuedentOfferByCID(@Param("cid") int cid, @Param("uid") Integer uid);

    @Select("SELECT COUNT(*) FROM offers WHERE uid = #{uid} AND cid is null and mid = #{mid} ")
    int hasStuedentOfferByMID(@Param("mid") int mid, @Param("uid") Integer uid);
}
