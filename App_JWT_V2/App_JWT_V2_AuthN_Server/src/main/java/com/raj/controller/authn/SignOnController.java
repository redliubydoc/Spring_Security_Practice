package com.raj.controller.authn;

import com.raj.AppConstants;
import com.raj.dto.GenericResponseDTO;
import com.raj.dto.SignOnRequestDTO;
import com.raj.exception.BadCredentialsException;
import com.raj.exception.RefreshTokenGenerationException;
import com.raj.exception.UserNotFoundException;
import com.raj.service.SignOnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/authn")
public class SignOnController {
    private final SignOnService signOnService;

    @Autowired
    public SignOnController(SignOnService signOnService) {
        this.signOnService = signOnService;
    }

    @PostMapping(path = "/so")
    public ResponseEntity<GenericResponseDTO> doSignOn(@RequestBody SignOnRequestDTO request) {

        ResponseEntity<GenericResponseDTO> response;
        String username = request.getUsername();
        String password = request.getPassword();

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            response = ResponseEntity.status(400).body(null);
        }
        else {
            try {
                Map<String, Object> data  = signOnService.doSignOn(username, password);
                response = ResponseEntity.status(200).body(
                    new GenericResponseDTO(Map.of(
                        "accessToken", data.get("accessToken"),
                        "refreshToken", data.get("refreshToken"),
                        "userId", data.get("userId"),
                        "userRole", data.get("role")
                    ), false, null)
                );
            }
            catch (UserNotFoundException e) {
                e.printStackTrace();
                response = ResponseEntity.status(401).body(
                    new GenericResponseDTO(null, true, Map.of(
                        "code", AppConstants.ERR_CODE_USER_NOT_FOUND,
                        "msg", "user not found"
                    ))
                );
            }
            catch (BadCredentialsException e) {
                e.printStackTrace();
                response = ResponseEntity.status(401).body(
                    new GenericResponseDTO(null, true, Map.of(
                        "code", AppConstants.ERR_CODE_INVALID_PASSWORD,
                        "msg", "invalid password"
                    ))
                );
            }
            catch (Exception e) {
                e.printStackTrace();
                response = ResponseEntity.status(500).body(null);
            }
        }

        return response;
    }
}
