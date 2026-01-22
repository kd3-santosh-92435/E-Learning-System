package com.elearning.service;

import com.elearning.dtos.ForgotPasswordDTO;
import com.elearning.dtos.ResetPasswordDTO;

public interface AuthService {

    void forgotPassword(ForgotPasswordDTO dto);

    void resetPassword(ResetPasswordDTO dto);
}
