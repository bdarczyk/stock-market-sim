package com.stockmarket;

public class main {
    public static void main(String[] args) {
        System.out.println("=== Tworzenie poprawnych akcji ===");
        Stock s1 = new Stock("CDR", "CD Projekt", 300.0);
        Stock s2 = new Stock("CDR", "CD Projekt RED", 400.0);
        Stock s3 = new Stock("PKN", "PKN Orlen", 100.0);

        System.out.println("s1 symbol: " + s1.getSymbol());
        System.out.println("s1 name: " + s1.getName());
        System.out.println("s1 initial price: " + s1.getInitialPrice());

        System.out.println("\n=== Test equals() ===");
        System.out.println("s1 equals s2? " + s1.equals(s2)); // true, ten sam symbol
        System.out.println("s1 equals s3? " + s1.equals(s3)); // false, różne symbole

        System.out.println("\n=== Test hashCode() ===");
        System.out.println("s1.hashCode(): " + s1.hashCode());
        System.out.println("s2.hashCode(): " + s2.hashCode());
        System.out.println("s3.hashCode(): " + s3.hashCode());

        System.out.println("\n=== Test tworzenia akcji z niepoprawnymi danymi ===");
        try {
            Stock invalid1 = new Stock(null, "Name", 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }

        try {
            Stock invalid2 = new Stock("SYM", null, 100.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }

        try {
            Stock invalid3 = new Stock("SYM", "Name", -1.0);
        } catch (IllegalArgumentException e) {
            System.out.println("Błąd: " + e.getMessage());
        }
    }
}
