package br.com.bytebank.accounts.application.service;

import br.com.bytebank.accounts.api.dtos.request.AccountRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.DepositRequestDTO;
import br.com.bytebank.accounts.api.dtos.request.WithdrawRequestDTO;
import br.com.bytebank.accounts.api.dtos.response.AccountResponseDTO;
import br.com.bytebank.accounts.api.dtos.response.BalanceResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface AccountService {

    AccountResponseDTO openAccount(AccountRequestDTO accountRequestDTO);

    AccountResponseDTO findAccountById(UUID uuid);

    void closeAccount(UUID id);

    List<AccountResponseDTO> showAccountsByBalance();

    void debit(WithdrawRequestDTO withdrawRequestDTO);

    void credit(DepositRequestDTO depositRequestDTO);

    List<AccountResponseDTO> listAccountByCostumer(UUID id);

    BalanceResponseDTO getBalance(UUID id);

    boolean existsByCustomer(UUID id);


}
