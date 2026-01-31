package com.stockmarket.logic;

import java.util.Comparator;

public class OrderAttractivenessComparator implements Comparator<Order> {
    @Override
    public int compare(Order a, Order b) {
        int c = a.getSide().ordinal() - b.getSide().ordinal();
        if (c != 0) return c;

        double pa = a.getLimitPrice();
        double pb = b.getLimitPrice();

        if (a.getSide() == OrderSide.BUY) {
            c = Double.compare(pb, pa); // wyższy limit pierwszy
        } else {
            c = Double.compare(pa, pb); // niższy limit pierwszy
        }
        if (c != 0) return c;

        return Long.compare(a.getSequence(), b.getSequence());
    }
}
