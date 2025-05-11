package  com.PetCaretopia.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "service_provider")
@Getter
@Setter
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
    private LocalDate creationalDate = LocalDate.now();

    public enum ServiceProviderType {
        VET, SITTER, TRAINER, OTHER
    }
}
