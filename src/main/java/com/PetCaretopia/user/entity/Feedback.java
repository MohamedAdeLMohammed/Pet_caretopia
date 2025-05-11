package  com.PetCaretopia.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "feedback")
@Getter
@Setter
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user; // Who gave the feedback

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "serviceProviderID", nullable = false)
    private ServiceProvider serviceProvider; // Feedback about which provider

    @Column(nullable = false, length = 500)
    private String feedbackContent;

    @Column(nullable = false)
    private LocalDate createdAt = LocalDate.now();
}
