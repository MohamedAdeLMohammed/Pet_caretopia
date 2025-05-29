package com.PetCaretopia.social.Service;

import com.PetCaretopia.Security.Service.CustomUserDetails;
import com.PetCaretopia.social.DTO.ReportDTO;
import com.PetCaretopia.social.Mapper.ReportMapper;
import com.PetCaretopia.social.entity.*;
import com.PetCaretopia.social.repository.*;
import com.PetCaretopia.user.entity.User;
import com.PetCaretopia.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@Slf4j

@Service
public class ReportService {
    @Autowired private ReportRepository reportRepository;
    @Autowired private ReportMapper reportMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private CommentRepository commentRepository;

    @Transactional
    public ReportDTO submitReport(ReportDTO dto, Long reporterId) {
        if (dto.getReportedUserId() == null &&
                dto.getReportedPostId() == null &&
                dto.getReportedCommentId() == null) {
            throw new IllegalArgumentException("You must report at least a user, post, or comment.");
        }

        User reporter = userRepository.findById(reporterId).orElseThrow();

        User reportedUser = dto.getReportedUserId() != null
                ? userRepository.findById(dto.getReportedUserId()).orElse(null)
                : null;

        Post reportedPost = dto.getReportedPostId() != null
                ? postRepository.findById(dto.getReportedPostId()).orElse(null)
                : null;

        Comment reportedComment = dto.getReportedCommentId() != null
                ? commentRepository.findById(dto.getReportedCommentId()).orElse(null)
                : null;

        Report report = reportMapper.toEntity(dto, reporter, reportedUser, reportedPost, reportedComment);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    public List<ReportDTO> getReportsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return reportRepository.findByReporter(user)
                .stream()
                .map(reportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ReportDTO changeReportStatus(Long reportId, ReportStatus status, CustomUserDetails principal) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setStatus(status);
        log.info("Admin [{}] changed status of report [{}] to [{}]",
                principal.getUsername(), reportId, status);
        return reportMapper.toDTO(reportRepository.save(report));
    }

    @Transactional
    public ReportDTO archiveReport(Long reportId, CustomUserDetails principal) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));
        report.setArchived(true);
        log.info("Admin [{}] archived report [{}]", principal.getUsername(), reportId);
        return reportMapper.toDTO(reportRepository.save(report));
    }
}
