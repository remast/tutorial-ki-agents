package remast.winkelshop.boundary;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.control.AuthControl;
import remast.winkelshop.entity.UserAccount;
import remast.winkelshop.security.AuthenticatedUser;

@RestController
@RequestMapping("/v1/auth")
public class AuthBoundary {

    private final AuthControl authControl;

    public AuthBoundary(AuthControl authControl) {
        this.authControl = authControl;
    }

    @PostMapping("/login")
    public DataResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthControl.LoginResult result = authControl.login(request.getEmail(), request.getPassword());
        UserAccount user = result.getUser();
        LoginResponse response = new LoginResponse(
                result.getAccessToken(),
                "Bearer",
                AuthControl.TOKEN_EXPIRES_IN_SECONDS,
                new AuthUserView(user.getId(), user.getName(), user.getRole())
        );
        return new DataResponse<>(response);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(Authentication authentication, HttpServletRequest request) {
        if (authentication == null) {
            return;
        }
        String header = request.getHeader("Authorization");
        String token = header != null && header.startsWith("Bearer ") ? header.substring(7) : null;
        if (token != null) {
            authControl.logout(token);
        }
    }

    public static class LoginRequest {
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private String accessToken;
        private String tokenType;
        private long expiresIn;
        private AuthUserView user;

        public LoginResponse(String accessToken, String tokenType, long expiresIn, AuthUserView user) {
            this.accessToken = accessToken;
            this.tokenType = tokenType;
            this.expiresIn = expiresIn;
            this.user = user;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public long getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(long expiresIn) {
            this.expiresIn = expiresIn;
        }

        public AuthUserView getUser() {
            return user;
        }

        public void setUser(AuthUserView user) {
            this.user = user;
        }
    }

    public static class AuthUserView {
        private String id;
        private String name;
        private String role;

        public AuthUserView(String id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }
}
