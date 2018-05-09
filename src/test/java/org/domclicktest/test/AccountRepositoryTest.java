package org.domclicktest.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;

import static org.springframework.transaction.TransactionDefinition.PROPAGATION_REQUIRES_NEW;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private PlatformTransactionManager transactionManager;
    private TransactionTemplate transactionTemplate;

    @Before
    public void init() {
        transactionTemplate = new TransactionTemplate(
                transactionManager, new DefaultTransactionAttribute(PROPAGATION_REQUIRES_NEW));
    }

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void shouldFailOnStaleObjectState() {
        Account account1 = transactionTemplate.execute(transactionStatus ->  repository.findById(1L).get());
        Account account2 = transactionTemplate.execute(transactionStatus ->  repository.findById(1L).get());

        transactionTemplate.execute(transactionStatus -> {
            account1.setAmount(BigDecimal.ONE);
            return repository.saveAndFlush(account1);
        });
        transactionTemplate.execute(transactionStatus -> repository.saveAndFlush(account2));
    }
}
