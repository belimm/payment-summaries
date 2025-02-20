import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentAggregator {

    private final List<Payment> payments;
    private final List<ExchangeRate> exchangeRates;




    public PaymentAggregator(List<Payment> payments, List<ExchangeRate> exchangeRates) {
        this.payments = payments;
        this.exchangeRates = exchangeRates;
    }


    /**
     * It takes payment object and tries to convert to EUR
     * @param payment
     * @return
     */
    private BigDecimal convertToEUR(Payment payment) {
        LocalDate date = payment.getDateTime().toLocalDate();
        String ccy = payment.getCurrency();

        // If the currency is already EUR, just return the payment amount
        if ("EUR".equalsIgnoreCase(ccy)) {
            return payment.getAmount();
        }

        return ExchangeRate.calculateExchangeRateGivenPairName(exchangeRates, payment, payment.getCurrency());
    }


    /**
     * This function's responsibility is finding highest single payment transaction
     * When I'm doing this, I took absolute value of it.
     * Suppose that there are three transactions in the file1.csv(-5000EUR, 2500EUR, 1000EUR), it should return -5000 as highest
     * @return
     */
    public BigDecimal highestEuroValueForSinglePayment() {
        BigDecimal highestEuroValue = BigDecimal.ZERO;

        for(Payment payment : payments) {
            BigDecimal paymentInEuro = convertToEUR(payment);

            if(highestEuroValue.abs().compareTo(paymentInEuro.abs()) < 0) {
                highestEuroValue = payment.getAmount();
            }
        }

        System.out.printf("Highest EUR value (for a single payment): %s%n", highestEuroValue);
        return highestEuroValue;
    }


    /**
     * This function's responsibility is finding lowest single payment transaction
     * When I'm doing this, I took absolute value of it.
     * Ex1: Suppose that there are three transactions in the file1.csv(5000EUR, -40EUR, 20EUR), it should return 20 as lowest
     * Ex2: Suppose that there are three transactions in the file1.csv(5000EUR, -20EUR, 40EUR), it should return -20 as lowest
     * @return
     */
    public BigDecimal lowestEuroValueForSinglePayment() {
        BigDecimal lowestEuroValue = new BigDecimal(String.valueOf(payments.get(0).getAmount()));

        for(int i=1; i<payments.size(); i++) {
            BigDecimal paymentInEuro = convertToEUR(payments.get(i));

            if(lowestEuroValue.abs().compareTo(paymentInEuro.abs()) > 0) {
                lowestEuroValue = payments.get(i).getAmount();
            }
        }
        System.out.printf("Lowest EUR value (for a single payment): %s%n", lowestEuroValue);
        return lowestEuroValue;
    }

    /**
     * It calculates outstanding amounts for each company in EUR (if there is another currency, it tries to convert into EUR, if there is no match file2.csv(exchange rates data) it takes as 0 EUR)
     */
    public void outStandingAmountsForEachCompany() {

        Map<String, List<BigDecimal>> companyAmounts = new HashMap<>();
        System.out.println("Outstanding amounts per company in EUR");
        for (Payment payment : this.payments) {
            BigDecimal eurAmount = convertToEUR(payment);
            // Optionally, check for null if conversion might fail:
            if (eurAmount != null) {
                companyAmounts.computeIfAbsent(payment.getCompany(), k -> new ArrayList<>()).add(eurAmount);
            }
        }

        for (Map.Entry<String, List<BigDecimal>> entry : companyAmounts.entrySet()) {
            // Compute the sum of all amounts for this company.
            BigDecimal total = entry.getValue().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Build a string representation like "100.00 + 200.00 + 50.00"
            String expression = entry.getValue().stream()
                    .map(BigDecimal::toString)
                    .collect(Collectors.joining(" + "));

            System.out.println("\t"+entry.getKey() + ": " + total + " = " + expression);
        }
    }


    /**
     * It calculates transactionVolume as absolute sum
     */
    public BigDecimal transactionVolume() {
        BigDecimal total = BigDecimal.ZERO;
        for(Payment payment : this.payments) {

            total = total.add(convertToEUR(payment).abs());
        }
        System.out.println("Transaction volume in EUR: " + total);
        return total;
    }

    /**
     * It calculates outstanding amount per currency
     */
    public void outstandingAmountPerCurrency() {
        Map<String, List<BigDecimal>> currencyAmounts = new HashMap<>();
        System.out.println("Outstanding amounts per currency");
        boolean exchangeRateNotFound = false;

        for (Payment payment : this.payments) {
            BigDecimal eurAmount = convertToEUR(payment);
            if(eurAmount.compareTo(BigDecimal.ZERO)  == 0) {
                exchangeRateNotFound = true;
            }
            // Optionally, check for null if conversion might fail:
            if (eurAmount != null) {
                currencyAmounts.computeIfAbsent(payment.getCurrency(), k -> new ArrayList<>()).add(eurAmount);
            }
        }

        // For each currency, compute and display the outstanding amount.
        for (Map.Entry<String, List<BigDecimal>> entry : currencyAmounts.entrySet()) {
            // Compute the sum of the EUR amounts.
            BigDecimal total = entry.getValue().stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Create a string representation like "100.00 + 200.00 + 50.00"
            String expression = entry.getValue().stream()
                    .map(BigDecimal::toString)
                    .collect(Collectors.joining(" + "));

            if(exchangeRateNotFound && total.compareTo(BigDecimal.ZERO) == 0) { //If there is no data file2.csv (then display as N/A)
                System.out.println("\t" + entry.getKey() + ": " + "N/A = N/A");
            }
            else{
                System.out.println("\t" + entry.getKey() + ": " + total + " = " + expression);
            }

        }
    }

    public void dailySummary() {
        highestEuroValueForSinglePayment(); //It calculates highest Euro(absolute value) value for single payment
        lowestEuroValueForSinglePayment(); //It calculates lowest Euro(absolute value) value for single payment
        outStandingAmountsForEachCompany(); //It calculates outstanding amounts for each company in file1.csv(where payment datas are located)
        transactionVolume(); //It calculates outstanding amounts for each company in file1.csv(where payment datas are located)
        outstandingAmountPerCurrency(); //It calculates
    }

    /**
     * Helper class to store the original payment and its EUR conversion (which may be null).
     */
    private static class PaymentConversionResult {
        Payment payment;
        BigDecimal eurValue;

        PaymentConversionResult(Payment payment, BigDecimal eurValue) {
            this.payment = payment;
            this.eurValue = eurValue;
        }
    }
}