package com.PetCaretopia.social.Mapper;

import com.PetCaretopia.social.DTO.ReportDTO;
import com.PetCaretopia.social.entity.Report;
import com.PetCaretopia.social.entity.ReportStatus;
import com.PetCaretopia.social.entity.Comment;
import com.PetCaretopia.social.entity.Post;
import com.PetCaretopia.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReportMapper {

    public Report toEntity(ReportDTO dto, User reporter, User reportedUser, Post reportedPost, Comment reportedComment) {
        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedUser(reportedUser);
        report.setReportedPost(reportedPost);
        report.setReportedComment(reportedComment);
        report.setReportReason(dto.getReportReason());

        // ✅ الحقول الجديدة
        report.setStatus(dto.getStatus() != null ? dto.getStatus() : ReportStatus.PENDING);
        report.setArchived(dto.isArchived());

        return report;
    }

    public ReportDTO toDTO(Report report) {
        ReportDTO dto = new ReportDTO();
        dto.setReportId(report.getReportId());
        dto.setReporterId(report.getReporter().getUserID());
        if (report.getReportedUser() != null) dto.setReportedUserId(report.getReportedUser().getUserID());
        if (report.getReportedPost() != null) dto.setReportedPostId(report.getReportedPost().getPostId());
        if (report.getReportedComment() != null) dto.setReportedCommentId(report.getReportedComment().getCommentId());
        dto.setReportReason(report.getReportReason());

        // ✅ الحقول الجديدة
        dto.setStatus(report.getStatus());
        dto.setArchived(report.isArchived());

        dto.setCreatedAt(report.getCreatedAt());
        return dto;
    }
}
