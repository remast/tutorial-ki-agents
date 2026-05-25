package remast.winkelshop.boundary;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import remast.winkelshop.common.DataResponse;
import remast.winkelshop.control.AuthControl;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthControl authControl;

    @PostMapping("/login")
    public DataResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthControl.LoginResult result = authControl.login(request.email(), request.password());
        return new DataResponse<>(
                new LoginResponse(
                        result.accessToken(),
                        "Bearer",
                        AuthControl.TOKEN_EXPIRES_IN_SECONDS,
                        new AuthUserView(result.user().id(), result.user().name(), result.user().role())
                )
        );
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

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {
    }

    public record LoginResponse(String accessToken, String tokenType, long expiresIn, AuthUserView user) {
    }

    public record AuthUserView(String id, String name, String role) {
    }
}
