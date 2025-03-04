package com.aplus.aplusmarket.repository;

import com.aplus.aplusmarket.documents.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends MongoRepository<ChatMessage,String> {

    /** 채팅방 Id로 메시지 조회
     * @param chatRoomId
     * @return
     */
    List<ChatMessage> findTop30ByChatRoomIdOrderByCreatedAtDesc(int chatRoomId);

    /** 채팅방 Id로 최근 메시지 조회
     * @param chatRoomId
     * @return
     */
    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(int chatRoomId);

    /**
     * 이전 메시지 조회
     * @param chatRoomId
     * @param createdAt
     * @return
     */
    List<ChatMessage> findTop30ByChatRoomIdAndCreatedAtBeforeOrderByCreatedAtDesc(
            int chatRoomId, LocalDateTime createdAt
    );
}
