package remast.winkelshop.auth.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.auth.entity.UserRepository;
import remast.winkelshop.auth.entity.UserRecord;
import remast.winkelshop.auth.boundary.AuthenticatedUser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    public static final long TOKEN_EXPIRES_IN_SECONDS = 3600;

    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    public LoginResult login(String email, String password) {
        var user = userRepository.findByEmail(email)
                .filter(account -> account.password().equals(password))
                .orElseThrow();

        var token = tokenStore.issue(new AuthenticatedUser(user.id(), user.role()));
        return new LoginResult(token, user);
    }

    public void logout(String token) {
        tokenStore.revoke(token);
    }

    public Optional<AuthenticatedUser> resolveUserByToken(String token) {
        return tokenStore.resolve(token);
    }

    public record LoginResult(String accessToken, UserRecord user) {
    }
}
