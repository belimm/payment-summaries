import java.math.BigDecimal;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.List;

public class ExchangeRate {
    private LocalDateTime dateTime;
    private String ccy1;
    private String ccy2;
    private BigDecimal rate;

    public ExchangeRate() {
    }

    public ExchangeRate(LocalDateTime dateTime, String ccy1, String ccy2, BigDecimal rate) {
        this.dateTime = dateTime;
        this.ccy1 = ccy1;
        this.ccy2 = ccy2;
        this.rate = rate;
    }

    public static BigDecimal calculateExchangeRateGivenPairName(List<ExchangeRate> exchangeRates, Payment payment, String ccy) {
        BigDecimal amount = new BigDecimal(0);



        for(ExchangeRate exchangeRate : exchangeRates) {
            if(exchangeRate.getCcy1().equals(ccy) && exchangeRate.getCcy2().equalsIgnoreCase("EUR")) {

                amount= payment.getAmount().multiply(exchangeRate.getRate());
            }
            else if(exchangeRate.getCcy2().equals(ccy) && exchangeRate.getCcy1().equalsIgnoreCase("EUR")) {
                amount= payment.getAmount().multiply(exchangeRate.getRate());

            }

        }

        return amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getCcy1() {
        return ccy1;
    }

    public void setCcy1(String ccy1) {
        this.ccy1 = ccy1;
    }

    public String getCcy2() {
        return ccy2;
    }

    public void setCcy2(String ccy2) {
        this.ccy2 = ccy2;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "dateTime=" + dateTime +
                ", ccy1='" + ccy1 + '\'' +
                ", ccy2='" + ccy2 + '\'' +
                ", rate=" + rate +
                '}';
    }
}