package com.stockmarket.logic;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaleReport {
    private final String ticker;
    private final LocalDate saleDate;
    private final int quantitySold;
    private final double unitSaleProceed;
    private final List<SaleLotResult> lotResults;

    private double totalProfitAndLoss;

    public SaleReport(String ticker, LocalDate saleDate, int quantitySold, double unitSaleProceed) {
        this.ticker = ticker;
        this.saleDate = saleDate;
        this.quantitySold = quantitySold;
        this.unitSaleProceed = unitSaleProceed;
        this.lotResults = new ArrayList<>();
    }

    public void addLotResult(SaleLotResult result) {
        lotResults.add(result);
        totalProfitAndLoss += result.getProfitAndLoss();
    }

    public String getTicker() {
        return ticker;
    }

    public LocalDate getSaleDate() {
        return saleDate;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public double getUnitSaleProceed() {
        return unitSaleProceed;
    }

    public double getTotalProfitAndLoss() {
        return totalProfitAndLoss;
    }

    public List<SaleLotResult> getLotResults() {
        return Collections.unmodifiableList(lotResults);
    }
}
