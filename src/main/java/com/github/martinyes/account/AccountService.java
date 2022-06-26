package com.github.martinyes.account;

import com.github.martinyes.Service;
import com.github.martinyes.wrapper.WrappedResult;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class AccountService implements Service<Account> {

    private final Map<UUID, Account> accounts = new ConcurrentHashMap<>();

    @Override
    public long load() {
        return -1;
    }

    @Override
    public Account create(Account account) {
        accounts.put(account.getUuid(), account);
        return account;
    }

    @Override
    public WrappedResult<Account> find(Predicate<Account> predicate) {
        return new WrappedResult<>(accounts.values(), predicate);
    }

    @Override
    public int totalBy(Predicate<Account> predicate) {
        return (int) accounts.values().stream().filter(predicate).count();
    }

    @Override
    public int total() {
        return accounts.size();
    }

    public void destroy(Account account) {
        account.destroyPreviewNPC(false);

        accounts.remove(account.getUuid());
    }

    public Map<UUID, Account> getAccounts() {
        return Collections.unmodifiableMap(this.accounts);
    }
}