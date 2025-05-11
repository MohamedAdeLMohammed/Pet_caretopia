package com.PetCaretopia.Chat.Repository;

import com.PetCaretopia.Chat.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("chatMessageRepository")
public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findByChatId(String chatId);
}
