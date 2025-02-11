package jiandgyu.jimechu.config.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final StringRedisTemplate redisTemplate;
    private static final Duration REFRESH_TOKEN_EXPIRE = Duration.ofDays(7);

    public void saveRefreshToken(String nickname, String refreshToken) {
        redisTemplate.opsForValue().set(getRefreshKey(nickname), refreshToken, REFRESH_TOKEN_EXPIRE);
    }

    public String getRefreshToken(String nickName) {
        return redisTemplate.opsForValue().get(getRefreshKey(nickName));
    }

    public void deleteRefreshToken(String nickname) {
        redisTemplate.delete(getRefreshKey(nickname));
    }

    public void blacklistAccessToken(String accessToken, Date expiration) {
        long ttl = (expiration.getTime() - System.currentTimeMillis()) / 1000; // 남은 유효기간 (초)
        if (ttl > 0) {
            redisTemplate.opsForValue().set(getBlacklistKey(accessToken), "blacklisted", Duration.ofSeconds(ttl));
        }
    }

    public boolean isValidateRefreshToken(String nickname, String accessToken) {
        String refreshToken = getRefreshToken(nickname);
        return refreshToken != null && refreshToken.equals(accessToken);
    }
    public boolean isBlacklisted(String accessToken) {
        return redisTemplate.hasKey(getBlacklistKey(accessToken));
    }

    private String getRefreshKey(String nickname) {
        return "refreshToken:" + nickname;
    }

    private String getBlacklistKey(String accessToken) {
        return "blacklist:accessToken:" + accessToken;
    }
}
