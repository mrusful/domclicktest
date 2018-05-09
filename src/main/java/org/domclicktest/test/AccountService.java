package org.domclicktest.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

@Service
@Validated
public class AccountService {

    private AccountRepository repository;

    @Autowired
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public void deposit(long accountId, @Min(1) BigDecimal amount) {
        Account account = getAccount(accountId);
        account.setAmount(account.getAmount().add(amount));
        repository.save(account);
    }

    public void withdraw(long accountId, @Min(1) BigDecimal amount) {
        Account account = getAccount(accountId);
        BigDecimal newAmount = account.getAmount().subtract(amount);

        checkGreaterThanZero(newAmount, account.getNumber());
        account.setAmount(newAmount);
        repository.save(account);
    }

    @Transactional
    public void transfer(long fromAccountId, long toAccountId, @Min(1) BigDecimal amount) {
        Account fromAccount = getAccount(fromAccountId);
        Account toAccount = getAccount(toAccountId);

        BigDecimal fromAccountNewAmount = fromAccount.getAmount().subtract(amount);
        checkGreaterThanZero(fromAccountNewAmount, fromAccount.getNumber());

        fromAccount.setAmount(fromAccountNewAmount);
        toAccount.setAmount(toAccount.getAmount().add(amount));

        repository.save(fromAccount);
        repository.save(toAccount);
    }

    private Account getAccount(long accountId) {
        return repository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Счет с идентификатором " + accountId + " не найден"));
    }

    private void checkGreaterThanZero(BigDecimal amount, String accountNumber) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Средств на счете " + accountNumber + " недостаточно");
        }
    }
}
