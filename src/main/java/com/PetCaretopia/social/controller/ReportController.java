package com.PetCaretopia.social.controller;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.social.DTO.ReportDTO;
import com.PetCaretopia.social.Service.ReportService;
import com.PetCaretopia.social.entity.ReportStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/social/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;


    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ReportDTO> fileReport(
            @Valid @RequestBody ReportDTO dto,
            @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
    ) {
        return ResponseEntity.ok(reportService.submitReport(dto, principal.getUserId()));
    }


    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(
            @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
    ) {
        return ResponseEntity.ok(reportService.getReportsByUser(principal.getUserId()));
    }


    @PatchMapping("/{reportId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatus status,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        return ResponseEntity.ok(reportService.changeReportStatus(reportId, status, principal));
    }

    @PatchMapping("/{reportId}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> archiveReport(
            @PathVariable Long reportId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        return ResponseEntity.ok(reportService.archiveReport(reportId, principal));
    }

}
