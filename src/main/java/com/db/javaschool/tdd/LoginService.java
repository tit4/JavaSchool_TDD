package com.db.javaschool.tdd;


/**
 * There are several rules for logging in to our system:
 * When logging in, a user provides a user name and password
 * The user name is a non-blank, unique string containing just about any characters.
 * The password is a non-blank, unique string containing just about any characters.
 * If a user attempts to log in and provides a valid account but invalid password three times, their account is revoked
 * However, if, for the same session, the user uses different account names, then the account will not be revoked (e.g. try to log in as yury1, yury2, yury1, three failed attempts but the user name is changed, no account gets revoked)
 * Not existing user can not login
 * A user cannot login to a revoked account
 * If failing attempts are not consecutive then no Revoke on Account should be invoked
 * A user must change their password every 30 days, so if they attempt to log in and the password is expired, then the user must change their password before they can successfully log in
 * A user cannot use any of their previous 24 passwords
 * User accounts have a time stamp of the last time the account was used (logged in, logged out, password changed). If an account has not been used for 45 days, then the account becomes revoked and can only be enabled by calling customer support.
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
