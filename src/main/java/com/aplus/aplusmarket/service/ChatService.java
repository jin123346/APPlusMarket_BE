package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.chat.response.ChatRoomCardResponseDTO;
import com.aplus.aplusmarket.entity.ChatMessage;
import com.aplus.aplusmarket.entity.ChatRoom;
import com.aplus.aplusmarket.mapper.chat.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

    final ChatRoomMapper chatRoomMapper;
    public List<ChatRoomCardResponseDTO> selectAllChatRoom() {

        return null;
    }

    public List<ChatMessage> selectAllMessagesByChatRoomId(int chatRoomId) {
        return chatRoomMapper.selectMessagesByChatRoomId(chatRoomId);
    }

    public List<ChatRoomCardResponseDTO> selectAllChatRoomCards() {
        return chatRoomMapper.selectAllChatRoomCards();
    }
}