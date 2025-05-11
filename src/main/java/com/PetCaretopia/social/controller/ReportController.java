package com.PetCaretopia.social.controller;

import com.PetCaretopia.social.DTO.ReportDTO;
import com.PetCaretopia.social.Service.ReportService;
import com.PetCaretopia.social.entity.ReportStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/social/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ReportDTO> fileReport(@Valid @RequestBody ReportDTO dto) {
        return ResponseEntity.ok(reportService.submitReport(dto));
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(reportService.getReportsByUser(userId));
    }

    // ✅ تغيير حالة البلاغ (مثلاً: RESOLVED أو DISMISSED)
    @PatchMapping("/{reportId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatus status
    ) {
        return ResponseEntity.ok(reportService.changeReportStatus(reportId, status));
    }

    // ✅ أرشفة بلاغ
    @PatchMapping("/{reportId}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> archiveReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.archiveReport(reportId));
    }
}
