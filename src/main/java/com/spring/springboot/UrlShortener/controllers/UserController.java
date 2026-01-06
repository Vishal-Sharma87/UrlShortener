package com.spring.springboot.UrlShortener.controllers;

import com.spring.springboot.UrlShortener.dto.UserCredentialUpdateRequestDto;
import com.spring.springboot.UrlShortener.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "User account management APIs (authenticated)"
)
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "Update logged-in user's credentials",
            description = """
            Updates the credentials of the currently authenticated user.

            Notes:
            - Username is resolved from JWT (SecurityContext)
            - Request body must not contain another user's identity
            - Only logged-in users can perform this action
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "User credentials updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request or update failed",
            content = @Content
    )
    @PutMapping("/update-user-credentials")
    public ResponseEntity<Void> updateUserCredentials(
            @RequestBody UserCredentialUpdateRequestDto user
    ) {
        try {
            String userName =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            userService.updateCredentials(user, userName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(
            summary = "Delete logged-in user account",
            description = """
            Permanently deletes the account of the currently authenticated user.

            Notes:
            - Operation is irreversible
            - All user-owned data (links, analytics, reports) may also be removed
            """
    )
    @ApiResponse(
            responseCode = "200",
            description = "User account deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content
    )
    @DeleteMapping("/delete-user")
    public ResponseEntity<Void> deleteUser() {
        try {
            String userName =
                    SecurityContextHolder.getContext().getAuthentication().getName();
            userService.deleteUser(userName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
