package br.com.coderbank.operacoes_bancarias.entities;



import br.com.coderbank.operacoes_bancarias.enums.OperationType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;


public class Transaction {
        private OperationType type; // DEPOSIT, WITHDRAW, TRANSFER
        private BigDecimal amount;
        private LocalDateTime dateTime;
        private String description;

        public Transaction(OperationType type, BigDecimal amount, String description, LocalDateTime date) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.dateTime = date;
        }

        public Transaction(OperationType type, BigDecimal amount, String description) {
            this.type = type;
            this.amount = amount;
            this.description = description;
            this.dateTime = LocalDateTime.now();
        }


        public Transaction(OperationType type, BigDecimal amount) {
            this.type = type;
            this.amount = amount;
            this.dateTime = LocalDateTime.now();
        }

        public OperationType getType() {
            return type;
        }

        public void setType(OperationType type) {
            this.type = type;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public LocalDateTime getDateTime() {
            return dateTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return type == that.type && Objects.equals(amount, that.amount) && Objects.equals(dateTime, that.dateTime) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, amount, dateTime, description);
    }

    @Override
    public String toString() {
        return "Transaction: " + type +
                ", Amount: $ " + amount +
                ", Date: " + dateTime +
                 '\n';
    }
}


