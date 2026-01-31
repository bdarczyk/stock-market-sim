package com.stockmarket.logic;

import java.time.LocalDate;

public class PurchaseLot {
    private final LocalDate purchaseDate;
    private final double unitPrice;
    private int quantity;

    public PurchaseLot(LocalDate purchaseDate, int quantity, double unitPrice) {
        require(purchaseDate != null, "date null");
        require(quantity > 0, "qty <= 0");
        require(unitPrice >= 0, "price < 0");
        this.purchaseDate = purchaseDate;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    private static void require(boolean ok, String msg) {
        if (!ok) throw new IllegalArgumentException(msg);
    }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public double getUnitPrice() { return unitPrice; }
    public int getQuantity() { return quantity; }

    public void reduceQuantity(int amount) {
        require(amount > 0, "amount <= 0");
        require(amount <= quantity, "amount > qty");
        quantity -= amount;
    }
}
