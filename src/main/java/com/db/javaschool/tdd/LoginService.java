package com.db.javaschool.tdd;


/**
 * There are several rules for logging in to our system:
 * When logging in, a user provides a user name and password
 * The user name is a non-blank, unique string containing just about any characters.
 * The password is a non-blank, unique string containing just about any characters.
 * Not existing user can not login
 * User is logged in if and only if provided password matches the Account password
 * When user is logged in successfully its Account is marked as loggedIn
 * If a user attempts to log in and provides a valid account but invalid password three times, their account is revoked
 * However, if, for the same session, the user uses different account names, then the account will not be revoked (e.g. try to log in as yury1, yury2, yury1, three failed attempts but the user name is changed, no account gets revoked)
 * If failing attempts are not consecutive then no Revoke on Account should be invoked
 * A user cannot login to a revoked account
 */

public class LoginService {

    private final IAccountRepository repo;

    public LoginService(IAccountRepository repo) {
        this.repo = repo;
    }

    public void login(String userName, String password) {
        IAccount acc = repo.find(userName);
    }
}
