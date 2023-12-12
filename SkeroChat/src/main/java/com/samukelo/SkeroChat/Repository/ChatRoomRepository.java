package com.samukelo.SkeroChat.Repository;

import com.samukelo.SkeroChat.Model.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<Chatroom, Long> {

    Optional<Chatroom> findBySenderIdAndRecipientId(String senderId, String recipientID);
}
