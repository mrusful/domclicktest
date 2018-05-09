package org.domclicktest.test;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@RestController(value = "account")
public class AccountController {

    private AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @PostMapping("/{accountId}/deposit")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deposit(@PathVariable long accountId, @RequestBody BigDecimal amount) {
        service.deposit(accountId, amount);
    }

    @PostMapping("/{accountId}/withdraw")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(@PathVariable long accountId, @RequestBody BigDecimal amount) {
        service.withdraw(accountId, amount);
    }

    @PostMapping("/transfer")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transfer(@RequestBody @Valid TransferRequest payload) {
        service.transfer(payload.fromAccountId, payload.toAccountId, payload.amount);
    }

    private static class TransferRequest {

        @NotNull
        private Long fromAccountId;

        @NotNull
        private Long toAccountId;

        @NotNull
        private BigDecimal amount;

        public Long getFromAccountId() {
            return fromAccountId;
        }

        public void setFromAccountId(Long fromAccountId) {
            this.fromAccountId = fromAccountId;
        }

        public Long getToAccountId() {
            return toAccountId;
        }

        public void setToAccountId(Long toAccountId) {
            this.toAccountId = toAccountId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
