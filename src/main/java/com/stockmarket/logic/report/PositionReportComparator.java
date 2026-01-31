package com.stockmarket.logic.report;

import com.stockmarket.logic.Position;

import java.util.Comparator;

/**
 * Sortowanie: Typ Aktywa -> Wartość Rynkowa (malejąco).
 */
public class PositionReportComparator implements Comparator<Position> {
    @Override
    public int compare(Position a, Position b) {
        if (a == b) {
            return 0;
        }

        int typeCmp = Integer.compare(a.getAsset().getType().getSortOrder(), b.getAsset().getType().getSortOrder());
        if (typeCmp != 0) {
            return typeCmp;
        }

        double aMarket = a.getAsset().getMarketValue(a.getTotalQuantity());
        double bMarket = b.getAsset().getMarketValue(b.getTotalQuantity());
        int valueCmp = Double.compare(bMarket, aMarket); // malejąco
        if (valueCmp != 0) {
            return valueCmp;
        }

        return a.getAsset().getTicker().compareTo(b.getAsset().getTicker());
    }
}
