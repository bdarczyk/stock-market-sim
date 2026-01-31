package com.stockmarket.logic.io;

import com.stockmarket.domain.Asset;
import com.stockmarket.domain.AssetType;
import com.stockmarket.domain.Commodity;
import com.stockmarket.domain.Currency;
import com.stockmarket.domain.Share;
import com.stockmarket.logic.Portfolio;
import com.stockmarket.logic.Position;
import com.stockmarket.logic.PurchaseLot;
import com.stockmarket.logic.exceptions.DataIntegrityException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;

public class PortfolioPersistence {
    private static final String SEP = "\\|";
    private static final java.util.Locale LOCALE = java.util.Locale.US;

    public void save(Path path, Portfolio portfolio) {
        if (path == null) throw new IllegalArgumentException("path null");
        if (portfolio == null) throw new IllegalArgumentException("portfolio null");

        try (BufferedWriter w = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            w.write("HEADER|CASH|" + String.format(LOCALE, "%.2f", portfolio.getCash()));
            w.newLine();

            for (Position p : portfolio.getPositionsSnapshot()) {
                Asset a = p.getAsset();
                int qty = p.getTotalQuantity();

                StringBuilder sb = new StringBuilder();
                sb.append("ASSET|")
                        .append(a.getType()).append("|")
                        .append(a.getTicker()).append("|")
                        .append(qty).append("|")
                        .append(String.format(LOCALE, "%.2f", a.getMarketPrice()));

                if (a.getType() == AssetType.COMMODITY) {
                    sb.append("|").append(String.format(LOCALE, "%.4f", ((Commodity) a).getStorageCostPerUnit()));
                } else if (a.getType() == AssetType.CURRENCY) {
                    sb.append("|").append(String.format(LOCALE, "%.4f", ((Currency) a).getSpread()));
                }

                w.write(sb.toString());
                w.newLine();

                for (PurchaseLot lot : p.getLotsSnapshot()) {
                    w.write("LOT|" + lot.getPurchaseDate()
                            + "|" + lot.getQuantity()
                            + "|" + String.format(LOCALE, "%.6f", lot.getUnitPrice()));
                    w.newLine();
                }
            }
        } catch (IOException e) {
            throw new DataIntegrityException("save failed", e);
        }
    }

    public Portfolio load(Path path) {
        if (path == null) throw new IllegalArgumentException("path null");

        try (BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line = r.readLine();
            if (line == null) throw new DataIntegrityException("empty");

            double cash = parseHeader(line);
            Portfolio pf = new Portfolio(cash);

            Position current = null;
            int declared = 0;
            int sum = 0;

            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;

                if (line.startsWith("HEADER|")) throw new DataIntegrityException("header not first");

                if (line.startsWith("ASSET|")) {
                    if (current != null) {
                        if (sum != declared) throw new DataIntegrityException("qty mismatch");
                        pf.addPositionFromPersistence(current);
                    }

                    String[] p = line.split(SEP);
                    if (p.length < 5) throw new DataIntegrityException("bad asset");

                    AssetType type;
                    try {
                        type = AssetType.valueOf(p[1]);
                    } catch (Exception e) {
                        throw new DataIntegrityException("bad type", e);
                    }

                    String ticker = p[2];
                    declared = parseInt(p[3]);
                    double market = parseDouble(p[4]);

                    Asset asset;
                    if (type == AssetType.SHARE) {
                        asset = new Share(ticker, market);
                    } else if (type == AssetType.COMMODITY) {
                        if (p.length != 6) throw new DataIntegrityException("commodity needs storage");
                        asset = new Commodity(ticker, market, parseDouble(p[5]));
                    } else if (type == AssetType.CURRENCY) {
                        if (p.length != 6) throw new DataIntegrityException("currency needs spread");
                        asset = new Currency(ticker, market, parseDouble(p[5]));
                    } else {
                        throw new DataIntegrityException("unsupported");
                    }

                    current = new Position(asset);
                    sum = 0;
                    continue;
                }

                if (line.startsWith("LOT|")) {
                    if (current == null) throw new DataIntegrityException("lot before asset");

                    String[] p = line.split(SEP);
                    if (p.length != 4) throw new DataIntegrityException("bad lot");

                    LocalDate date;
                    try {
                        date = LocalDate.parse(p[1]);
                    } catch (Exception e) {
                        throw new DataIntegrityException("bad date", e);
                    }

                    int qty = parseInt(p[2]);
                    double unit = parseDouble(p[3]);

                    current.addLot(date, qty, unit);
                    sum += qty;
                    continue;
                }

                throw new DataIntegrityException("unknown line");
            }

            if (current != null) {
                if (sum != declared) throw new DataIntegrityException("qty mismatch");
                pf.addPositionFromPersistence(current);
            }

            return pf;
        } catch (IOException e) {
            throw new DataIntegrityException("load failed", e);
        }
    }

    private double parseHeader(String line) {
        String[] p = line.split(SEP);
        if (p.length != 3) throw new DataIntegrityException("bad header");
        if (!"HEADER".equals(p[0]) || !"CASH".equals(p[1])) throw new DataIntegrityException("bad header");
        return parseDouble(p[2]);
    }

    private int parseInt(String raw) {
        try {
            int v = Integer.parseInt(raw);
            if (v < 0) throw new DataIntegrityException("neg int");
            return v;
        } catch (NumberFormatException e) {
            throw new DataIntegrityException("bad int", e);
        }
    }

    private double parseDouble(String raw) {
        try {
            double v = Double.parseDouble(raw);
            if (v < 0) throw new DataIntegrityException("neg double");
            return v;
        } catch (NumberFormatException e) {
            throw new DataIntegrityException("bad double", e);
        }
    }
}
