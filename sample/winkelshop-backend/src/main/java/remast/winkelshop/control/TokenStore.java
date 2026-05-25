package remast.winkelshop.control;

import org.springframework.stereotype.Component;
import remast.winkelshop.security.AuthenticatedUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, AuthenticatedUser> tokenToUser = new ConcurrentHashMap<>();

    public String issue(AuthenticatedUser user) {
        String token = UUID.randomUUID().toString();
        tokenToUser.put(token, user);
        return token;
    }

    public void revoke(String token) {
        tokenToUser.remove(token);
    }

    public Optional<AuthenticatedUser> resolve(String token) {
        return Optional.ofNullable(tokenToUser.get(token));
    }
}
