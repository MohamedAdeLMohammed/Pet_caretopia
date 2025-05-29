package com.PetCaretopia.Chat.Repository;


import com.PetCaretopia.Chat.Model.Account;
import com.PetCaretopia.Chat.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByUsername(String username);
    List<Account> findAllByStatus(Status status);


}
