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

    @Select("""
            SELECT c.id AS chatRoomId,
                   p.product_images AS productThumbnail,
                   p.id AS productId,
        
                   CASE WHEN c.seller_id = #{myUserId} THEN TRUE ELSE FALSE END AS isSeller,
                   u.profile_img AS userImage,
                   u.name AS userName,
                   m.content AS recentMessage,
                   m.created_at AS messageCreatedAt
            FROM tb_chat_room AS c
            LEFT JOIN tb_chat_message AS m ON c.id = m.chat_room_id
            JOIN tb_chat_mapping AS cm ON cm.chat_room_id = c.id
            JOIN tb_product AS p ON p.id = c.product_id
            JOIN tb_user AS u ON u.id = cm.user_id
            WHERE cm.user_id != #{myUserId} -- 상대방 사용자 정보만 가져오기
            ORDER BY m.created_at DESC
            LIMIT 1
            """)
    List<ChatRoomCardResponseDTO> selectAllChatRoomCards(@Param("myUserId") int myUserId);

    List<ChatRoom> selectAllChatRooms();
    List<ChatMessage> selectMessagesByChatRoomId(int chatRoomId);
}