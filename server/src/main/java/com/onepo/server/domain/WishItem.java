package com.onepo.server.domain;


import com.onepo.server.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
public class WishItem {

    @Id
    @GeneratedValue
    @Column(name="WISH_ITEM_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name="WISH_ID")
    private Wish wish;

    @ManyToOne
    @JoinColumn(name="ITEM_ID")
    private Item item;
}