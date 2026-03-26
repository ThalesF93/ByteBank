package br.com.coderbank.operacoes_bancarias.entities.contas;


import br.com.coderbank.operacoes_bancarias.enums.FeeType;
import br.com.coderbank.operacoes_bancarias.enums.OperationType;

import java.math.BigDecimal;

public class SavingsAccount extends Account{
    public SavingsAccount(Holder holder) {
        super(holder);
    }

    @Override
    public void transference(Account destinationAccount, BigDecimal amount) {
       amountValidation(amount);
       BigDecimal fee = calculateFee(FeeType.SAVINGS_ACCOUNT_TRANSFER.getRate(), amount);
       balanceValidation(amount.add(fee));
       this.balance = this.balance.subtract(amount).subtract(fee);
       destinationAccount.deposit(amount);
       addTransactions((new Transaction(OperationType.TRANSFER, amount)));
       addTransactions((new Transaction(OperationType.FEE, fee)));
    }

    public BigDecimal calculateFee(BigDecimal tax, BigDecimal amount) {
        return amount.multiply(tax);
    }
}
