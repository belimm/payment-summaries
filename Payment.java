import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {
    private LocalDateTime dateTime;
    private String company;
    private String currency;
    private BigDecimal amount;

    public Payment() {
    }

    public Payment(LocalDateTime dateTime, String company, String currency, BigDecimal amount) {
        this.dateTime = dateTime;
        this.company = company;
        this.currency = currency;
        this.amount = amount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getCompany() {
        return company;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "dateTime=" + dateTime +
                ", company='" + company + '\'' +
                ", currency='" + currency + '\'' +
                ", amount=" + amount +
                '}';
    }
}