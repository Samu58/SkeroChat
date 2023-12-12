package com.samukelo.SkeroChat.Service;

import com.samukelo.SkeroChat.Model.Chatroom;
import com.samukelo.SkeroChat.Repository.ChatRoomRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Builder
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExist){
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(Chatroom::getChatId)
                .or(() -> {
                    if (createNewRoomIfNotExist){
                        var chatId = createChatId(senderId, recipientId);

                        return Optional.of(chatId);
                    }
                    return Optional.empty();
                });
    }

    private String createChatId(String senderId, String recipientId) {

        var chatId = String.format("%s_%s", senderId, recipientId);  //this will return name like this senderName_recepentName

        Chatroom senderRecipient = Chatroom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        Chatroom recipientSender = Chatroom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);
        return chatId;
    }

}
