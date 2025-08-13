package es.merlinsoftware;

import es.merlinsoftware.pojo.ProductSales;
import es.merlinsoftware.pojo.ProductStock;

import java.util.*;
import java.util.stream.Collectors;

public class Solution {

    public static List<Long> sortProductsByScores(int stockWeight, int salesWeight,
                                                  List<ProductStock> productsStockInformation,
                                                  List<ProductSales> productsSalesInformation) {
        if (stockWeight + salesWeight != 100) {
            throw new IllegalArgumentException("Weights must add to 100");
        }

        Map<Long, Pair<Double, Long>> infoMap = new HashMap<>();

        productsSalesInformation
            .stream()
            .forEach(sale -> infoMap.put(
                sale.getProductId(),
                new Pair<Double, Long>(sale.getSalesAmount(), 0L))
            );
        productsStockInformation
            .stream()
            .forEach(stock -> 
                infoMap.merge(
                    stock.getProductId(),
                    new Pair<>(0.0, stock.getAvailableStock()),
                    (foundEntry, newEntry) -> new Pair<>(foundEntry.a, newEntry.b))
            );

        Map<Long, Double> scores = new HashMap<>();

        infoMap.entrySet().stream().forEach(entry-> {
            double saleAmount = entry.getValue().a;
            long availableStock = entry.getValue().b;
            
            double score = (stockWeight / 100.0 * availableStock) + (salesWeight / 100.0 * saleAmount);

            scores.put(entry.getKey(), score);
        });

        List<Long> result = scores
            .entrySet()
            .stream()
            .sorted(
                (s1, s2) -> 
                    Double.compare(s2.getValue(), s1.getValue())
            )
            .map(entry -> entry.getKey())
            .collect(Collectors.toList())
        ;

        return result;
    }
}

class Pair<A, B> {
    A a;
    B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }
    public A getA() { return this.a; }
    public B getB() { return this.b; }
    public void setA(A a) { this.a = a; }
    public void setB(B b) { this.b = b; }
}
