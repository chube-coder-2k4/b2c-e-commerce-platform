package dev.commerce.services.impl;

import dev.commerce.entitys.RefreshToken;
import dev.commerce.entitys.Users;
import dev.commerce.repositories.redis.RefreshTokenRepository;
import dev.commerce.services.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    @Override
    public void deleteByUserId(Users userId) {
        refreshTokenRepository.deleteByUsersId(userId);
    }

    @Override
    public String saveRefreshToken(RefreshToken refreshToken) {
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        return savedToken.getToken();
    }

    @Override
    public RefreshToken getByUserId(Users userId) {
        return refreshTokenRepository.findByUsersId(userId);
    }
}
