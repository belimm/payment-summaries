import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        List<Payment> payments = new ArrayList<>();
        List<ExchangeRate> exchangeRates = new ArrayList<>();


        CSVReader csvReader = new CSVReader();
        payments = csvReader.readPaymentCSV("./input/file1.csv"); //read file1.csv
        exchangeRates = csvReader.readExhangeRateCSV("./input/file2.csv"); // read file2.csv


        // Build the aggregator with payments data & exchangeRates data
        PaymentAggregator aggregator = new PaymentAggregator(payments, exchangeRates);
        aggregator.dailySummary(); // do the operation of dailySummary
        // Compute daily summaries
    }
}