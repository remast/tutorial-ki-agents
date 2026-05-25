package remast.winkelshop.auth.control;

import org.springframework.stereotype.Component;
import remast.winkelshop.auth.boundary.AuthenticatedUser;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenStore {

    private final Map<String, AuthenticatedUser> tokenToUser = new ConcurrentHashMap<>();

    public String issue(AuthenticatedUser user) {
        var token = UUID.randomUUID().toString();
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
