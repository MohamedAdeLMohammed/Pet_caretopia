package  com.PetCaretopia.user.entity;
import com.PetCaretopia.pet.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.util.List;

@Entity
@Table(name = "pet_owners")
@Getter
@Setter
public class PetOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long petOwnerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user; //   PetOwner is linked to a User

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets; //   Correctly references 'owner' in Pet
}
