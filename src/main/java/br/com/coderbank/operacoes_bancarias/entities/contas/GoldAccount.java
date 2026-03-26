package br.com.coderbank.operacoes_bancarias.entities.contas;



import br.com.coderbank.operacoes_bancarias.enums.FeeType;
import br.com.coderbank.operacoes_bancarias.enums.OperationType;
import br.com.coderbank.operacoes_bancarias.exceptions.InsufficientBalanceException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class GoldAccount extends Account{

    private static final DecimalFormat US_FORMATTER = new DecimalFormat("¤#,##0.00",
            new DecimalFormatSymbols(Locale.US));

    private BigDecimal overdraft = new BigDecimal("10000");


    public GoldAccount(Holder holder) {
        super(holder);
    }

    public BigDecimal getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(BigDecimal overdraft) {
        this.overdraft = overdraft;
    }

    public BigDecimal getAvailableOverdraft() {
        if (getBalance().compareTo(BigDecimal.ZERO) >= 0) {
            return overdraft;
        }
        return overdraft.add(getBalance());
    }

    @Override
    protected void balanceValidation(BigDecimal amount) {
        if (getBalance().add(overdraft).compareTo(amount) < 0){
            throw new InsufficientBalanceException("Unauthorized operation! Operation must not be more than balance");
        }
    }

    @Override
    public void transference(Account destinationAccount, BigDecimal amount) {
        amountValidation(amount);

        BigDecimal fee = calculateFee(FeeType.GOLD_ACCOUNT_TRANSFER.getRate(), amount);

        balanceValidation(amount.add(fee));
        this.balance = this.balance.subtract(amount).subtract(fee);

        destinationAccount.deposit(amount);
        addTransactions((new Transaction(OperationType.TRANSFER, amount)));
        addTransactions((new Transaction(OperationType.FEE, fee)));
    }

    @Override
    protected void writeExtraInfo(BufferedWriter writer) throws IOException {
        writer.write("- Overdraft available: " + US_FORMATTER.format(getAvailableOverdraft()) + "\n");
    }

    public BigDecimal calculateFee(BigDecimal tax, BigDecimal amount) {
        return amount.multiply(tax);
    }

}
