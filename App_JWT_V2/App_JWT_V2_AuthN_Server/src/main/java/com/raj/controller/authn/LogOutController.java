package com.raj.controller.authn;

import com.raj.AppConstants;
import com.raj.dto.GenericResponseDTO;
import com.raj.dto.LogOutRequestDTO;
import com.raj.exception.*;
import com.raj.service.LogOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/authn")
public class LogOutController {
    private final LogOutService logOutService;

    @Autowired
    public LogOutController(LogOutService logOutService) {
        this.logOutService = logOutService;
    }

    @PostMapping(path = "/lo")
    public ResponseEntity<GenericResponseDTO> doLogOut(@RequestBody LogOutRequestDTO request) {

        ResponseEntity<GenericResponseDTO> response;
        String accessToken = request.getAccessToken();
        String refreshToken = request.getRefreshToken();

        System.out.println(request);

        if (accessToken == null || refreshToken == null || accessToken.isEmpty() || refreshToken.isEmpty()) {
            response = ResponseEntity.status(400).body(null);
        }
        else {
            try {
                logOutService.doLogOut(accessToken, refreshToken);
                response = ResponseEntity.status(200).body(null);
            }
            catch (InvalidRefreshTokenException e) {
                e.printStackTrace();
                response = ResponseEntity.status(401).body(
                    new GenericResponseDTO(null, true, Map.of(
                        "code", AppConstants.ERR_CODE_INVALID_REFRESH_TOKEN,
                        "msg", "invalid refreshToken"
                    ))
                );
            }
            catch (InvalidAccessTokenException e) {
                e.printStackTrace();
                response = ResponseEntity.status(401).body(
                    new GenericResponseDTO(null, true, Map.of(
                        "code", AppConstants.ERR_CODE_INVALID_ACCESS_TOKEN,
                        "msg", "invalid accessToken"
                    ))
                );
            }
            catch (ExpiredAccessTokenException e) {
                e.printStackTrace();
                response = ResponseEntity.status(401).body(
                    new GenericResponseDTO(null, true, Map.of(
                        "code", AppConstants.ERR_CODE_EXPIRED_ACCESS_TOKEN,
                        "msg", "expired accessToken"
                    ))
                );
            }
            catch (Exception e) {
                response = ResponseEntity.status(500).body(null);
            }
        }

        return response;
    }
}
