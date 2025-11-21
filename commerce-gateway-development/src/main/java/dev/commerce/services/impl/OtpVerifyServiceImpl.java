package dev.commerce.services.impl;

import dev.commerce.redis.OtpVerify;
import dev.commerce.repositories.redis.OtpVerifyRepository;
import dev.commerce.services.OtpVerifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OtpVerifyServiceImpl implements OtpVerifyService {
    private final OtpVerifyRepository otpVerifyRepository;


    @Override
    public boolean verifyOtp(String email, String otp) {
        String storedOtp = otpVerifyRepository.findByEmail(email).getOtp();
        if(!storedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        otpVerifyRepository.delete(otpVerifyRepository.findByEmail(email));
        return true;
    }

    @Override
    public void saveOtp(String email, String otp) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if(otp == null || otp.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP cannot be null or empty");
        }
        OtpVerify otpVerify = OtpVerify.builder()
                .email(email)
                .otp(otp)
                .build();
        otpVerifyRepository.save(otpVerify);
    }

    @Override
    public String getOtp(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        OtpVerify otpVerify = otpVerifyRepository.findByEmail((email));
        if(otpVerify == null) {
            throw new IllegalArgumentException("No OTP found for the provided email");
        }
        return otpVerify.getOtp();
    }

    @Override
    public void deleteOtp(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        OtpVerify otpVerify = otpVerifyRepository.findByEmail(email);
        if(otpVerify != null) {
            otpVerifyRepository.delete(otpVerify);
        }
    }

}
