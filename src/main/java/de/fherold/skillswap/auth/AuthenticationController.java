package de.fherold.skillswap.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.fherold.skillswap.exception.ErrorResponse;
import de.fherold.skillswap.model.User;
import de.fherold.skillswap.repository.UserRepository;
import de.fherold.skillswap.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Secure entry point for the SkillSwap SaaS. Handles identity verification and tenant-aware token issuance.")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Operation(summary = "User Login", description = "Validates user credentials. On success, issues a JWT valid for 8h. The token includes the user's role and assigned tenantId for data isolation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT successfully issued", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Authentication failed (Wrong username/password)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "Invalid Credentials", value = """
                    {
                      "timestamp": "2026-03-16T10:00:00",
                      "message": "Invalid username or password",
                      "errorCode": "BAD_CREDENTIALS"
                    }
                    """))),
            @ApiResponse(responseCode = "400", description = "Invalid request format (e.g. missing body)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(name = "Malformed Request", value = """
                    {
                      "timestamp": "2026-03-16T10:00:00",
                      "message": "Required request body is missing or malformed",
                      "errorCode": "MALFORMED_JSON"
                    }
                    """)))
    })
    @PostMapping(value = "/login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User credentials") @RequestBody AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user, user.getTenantId());

        return ResponseEntity.ok(
                AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build());
    }
}
