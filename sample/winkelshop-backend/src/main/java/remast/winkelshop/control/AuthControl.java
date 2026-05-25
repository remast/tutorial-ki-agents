package remast.winkelshop.control;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import remast.winkelshop.entity.UserRepository;
import remast.winkelshop.entity.UserAccount;
import remast.winkelshop.entity.UserRecord;
import remast.winkelshop.security.AuthenticatedUser;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthControl {

    public static final long TOKEN_EXPIRES_IN_SECONDS = 3600;

    private final UserRepository userRepository;
    private final TokenStore tokenStore;

    public LoginResult login(String email, String password) {
        UserRecord user = userRepository.findByEmail(email)
                .filter(account -> account.password().equals(password))
                .orElseThrow();

        String token = tokenStore.issue(new AuthenticatedUser(user.id(), user.role()));
        return new LoginResult(token, new UserAccount(user.id(), user.name(), user.email(), user.password(), user.role()));
    }

    public void logout(String token) {
        tokenStore.revoke(token);
    }

    public Optional<AuthenticatedUser> resolveUserByToken(String token) {
        return tokenStore.resolve(token);
    }

    public record LoginResult(String accessToken, UserAccount user) {
    }
}
