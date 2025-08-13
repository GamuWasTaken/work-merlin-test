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

        Map<Long, Double> salesMap = new HashMap<>();

        productsSalesInformation
            .stream()
            .forEach(sale -> salesMap.put(sale.getProductId(), sale.getSalesAmount()));

        Map<Long, Double> scores = new HashMap<>();

        productsStockInformation.stream().forEach(stock -> {
            double saleAmount = salesMap.getOrDefault(stock.getProductId(), 0.0);
            double score = (stockWeight / 100.0 * stock.getAvailableStock()) + (salesWeight / 100.0 * saleAmount);

            scores.put(stock.getProductId(), score);
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
