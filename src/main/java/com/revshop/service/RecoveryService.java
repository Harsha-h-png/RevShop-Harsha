package com.revshop.service;

import com.revshop.dao.PasswordResetDao;

public class RecoveryService {

    private PasswordResetDao resetDao = new PasswordResetDao();

    public void resetPassword(String email, String newPassword) {
        resetDao.updatePassword(email, newPassword);
    }
}
