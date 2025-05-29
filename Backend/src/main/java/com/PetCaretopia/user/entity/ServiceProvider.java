package  com.PetCaretopia.user.entity;

import com.PetCaretopia.facility.entity.Facility;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "service_provider")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long serviceProviderID;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal serviceProviderSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceProviderType serviceProviderType; // Example: Vet, Groomer, Trainer

    @Column(nullable = false)
    private Integer serviceProviderExperience; // Number of years

    @Column(nullable = false)
    private LocalDateTime creationalDate = LocalDateTime.now();

    public enum ServiceProviderType {
        VET, SITTER, TRAINER, OTHER
    }
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "serviceProviderFacility",
            joinColumns = @JoinColumn(name = "serviceProviderID"),
            inverseJoinColumns = @JoinColumn(name = "facility_id")
    )
    private List<Facility> facilities;
    @OneToMany(mappedBy = "serviceProvider", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Feedback> feedbacks;
}
