package com.stockmarket.logic.report;

import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.Position;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PortfolioReportGenerator {
    public String generateReport(Portfolio portfolio) {
        if (portfolio == null) {
            throw new IllegalArgumentException("portfolio nie może być null");
        }

        List<Position> positions = new ArrayList<>(portfolio.getPositionsSnapshot());
        Collections.sort(positions, new PositionReportComparator());

        StringBuilder sb = new StringBuilder();
        sb.append("CASH|").append(String.format(java.util.Locale.US, "%.2f", portfolio.getCash())).append("\n");

        for (Position p : positions) {
            double marketValue = p.getAsset().getMarketValue(p.getTotalQuantity());
            sb.append("ASSET|")
                    .append(p.getAsset().getType())
                    .append("|")
                    .append(p.getAsset().getTicker())
                    .append("|")
                    .append(p.getTotalQuantity())
                    .append("|")
                    .append(String.format(java.util.Locale.US, "%.2f", marketValue))
                    .append("\n");
        }
        return sb.toString();
    }
}
