package com.raj.controller.authn;

import com.raj.AppConstants;
import com.raj.dto.GenericResponseDTO;
import com.raj.dto.RefreshRequestDTO;
import com.raj.exception.*;
import com.raj.service.RefreshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping(path = "/authn")
public class RefreshController {
    private final RefreshService refreshService;

    @Autowired
    public RefreshController(RefreshService refreshService) {
        this.refreshService = refreshService;
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<GenericResponseDTO> doRefresh(@RequestBody RefreshRequestDTO request) {

        ResponseEntity<GenericResponseDTO> response;
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isEmpty()) {
            response = ResponseEntity.status(400).body(null);
        }
        else {
            try {
                Map<String, Object> data = refreshService.doRefresh(refreshToken);
                response = ResponseEntity.status(200).body(
                    new GenericResponseDTO(data, false, null)
                );
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
            catch (Exception e) {
                e.printStackTrace();
                response = ResponseEntity.status(500).body(null);
            }
        }

        return response;
    }
}
