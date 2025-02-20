import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {
    private List<Payment> paymentList;
    private List<ExchangeRate> exchangeRateList;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");



    public CSVReader() {
        this.paymentList = new ArrayList<Payment>();
        this.exchangeRateList = new ArrayList<ExchangeRate>();

    }

    public List<Payment> getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(List<Payment> paymentList) {
        this.paymentList = paymentList;
    }

    public List<ExchangeRate> getExchangeRateList() {
        return exchangeRateList;
    }

    public void setExchangeRateList(List<ExchangeRate> exchangeRateList) {
        this.exchangeRateList = exchangeRateList;
    }

    public List<Payment> readPaymentCSV(String filePath) throws IOException {
        List<Payment> payments = new ArrayList<>();

        // Read file
        List<String> lines = Files.readAllLines(Paths.get(filePath));


        for(String line : lines) {
            String[] splittedLine = line.split(";");
            if(splittedLine.length == 4) { //Valid data
                payments.add(new Payment(LocalDateTime.parse(splittedLine[0], formatter), splittedLine[1], splittedLine[2], new BigDecimal(splittedLine[3])));
            }
        }
        return payments;
    }

    public List<ExchangeRate> readExhangeRateCSV(String filePath) throws IOException {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        // Read file
        List<String> lines = Files.readAllLines(Paths.get(filePath));


        for(String line : lines) {
            String[] splittedLine = line.split(";");
            if(splittedLine.length == 4) { //Valid data
                exchangeRates.add(new ExchangeRate(LocalDateTime.parse(splittedLine[0], formatter), splittedLine[1], splittedLine[2], new BigDecimal(splittedLine[3])));
            }
        }
        return exchangeRates;
    }
}
