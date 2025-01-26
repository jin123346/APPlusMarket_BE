package com.aplus.aplusmarket.mapper.chat;

import com.aplus.aplusmarket.dto.chat.response.ChatRoomCardResponseDTO;
import com.aplus.aplusmarket.entity.ChatRoom;
import com.aplus.aplusmarket.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatRoomMapper {

    @Select("SELECT c.id AS chatRoomId, m.content AS recentMessage, m.created_at AS messageCreatedAt " +
            "FROM tb_chat_room c " +
            "LEFT JOIN tb_chat_message m ON c.id = m.chat_room_id " +
            "ORDER BY m.created_at DESC")
    List<ChatRoomCardResponseDTO> selectAllChatRoomCards();

    List<ChatRoom> selectAllChatRooms();
    List<ChatMessage> selectMessagesByChatRoomId(int chatRoomId);
}