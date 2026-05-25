package remast.winkelshop.control;

import org.springframework.stereotype.Service;
import remast.winkelshop.entity.SeedData;
import remast.winkelshop.entity.UserAccount;
import remast.winkelshop.security.AuthenticatedUser;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthControl {

    public static final long TOKEN_EXPIRES_IN_SECONDS = 3600;

    private final SeedData seedData;

    public AuthControl(SeedData seedData) {
        this.seedData = seedData;
    }

    public LoginResult login(String email, String password) {
        UserAccount user = seedData.findUserByEmail(email)
                .filter(account -> account.getPassword().equals(password))
                .orElseThrow(() -> new UnauthorizedException("invalid credentials"));

        String token = UUID.randomUUID().toString();
        seedData.getTokenToUserId().put(token, user.getId());
        return new LoginResult(token, user);
    }

    public void logout(String token) {
        seedData.getTokenToUserId().remove(token);
    }

    public Optional<AuthenticatedUser> resolveUserByToken(String token) {
        String userId = seedData.getTokenToUserId().get(token);
        if (userId == null) {
            return Optional.empty();
        }
        return seedData.findUserById(userId)
                .map(user -> new AuthenticatedUser(user.getId(), user.getRole()));
    }

    public static class LoginResult {

        private final String accessToken;
        private final UserAccount user;

        public LoginResult(String accessToken, UserAccount user) {
            this.accessToken = accessToken;
            this.user = user;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public UserAccount getUser() {
            return user;
        }
    }
}
