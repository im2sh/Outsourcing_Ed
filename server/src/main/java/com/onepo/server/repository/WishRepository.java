package com.onepo.server.repository;


import com.onepo.server.domain.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<Wish,Integer> {

    Wish findByMemberId(Long id);
    Wish findWishById(Long id);
    Wish findWishByMemberId(Long id);



    @Override
    List<Wish> findAll();




}
