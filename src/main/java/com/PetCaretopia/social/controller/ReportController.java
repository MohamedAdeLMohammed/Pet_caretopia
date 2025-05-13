package com.PetCaretopia.social.controller;

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

    /**
     * ✅ تقديم بلاغ جديد (على بوست أو كومنت)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER')")
    public ResponseEntity<ReportDTO> fileReport(@Valid @RequestBody ReportDTO dto) {
        return ResponseEntity.ok(reportService.submitReport(dto));
    }

    /**
     * ✅ استرجاع جميع البلاغات المقدمة من المستخدم الحالي
     */
    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'PET_OWNER', 'SERVICE_PROVIDER', 'ADMIN')")
    public ResponseEntity<List<ReportDTO>> getReportsByUser(
            @AuthenticationPrincipal com.PetCaretopia.Security.Service.CustomUserDetails principal
    ) {
        return ResponseEntity.ok(reportService.getReportsByUser(principal.getUserId()));
    }

    /**
     * ✅ تغيير حالة بلاغ (بواسطة الأدمن فقط)
     */
    @PatchMapping("/{reportId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> updateStatus(
            @PathVariable Long reportId,
            @RequestParam ReportStatus status
    ) {
        return ResponseEntity.ok(reportService.changeReportStatus(reportId, status));
    }

    /**
     * ✅ أرشفة بلاغ (بواسطة الأدمن فقط)
     */
    @PatchMapping("/{reportId}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReportDTO> archiveReport(@PathVariable Long reportId) {
        return ResponseEntity.ok(reportService.archiveReport(reportId));
    }
}
