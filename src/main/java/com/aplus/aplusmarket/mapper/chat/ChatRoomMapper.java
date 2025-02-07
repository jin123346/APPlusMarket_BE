package com.aplus.aplusmarket.mapper.chat;
import com.aplus.aplusmarket.dto.chat.response.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
*  2025.02.03 황수빈 - 채팅방 상세정보 조회 메서드 추가
*
*/

@Mapper
public interface ChatRoomMapper {
    @Insert("INSERT INTO tb_chat_message (chat_room_id, content, user_id, created_at) " +
            "VALUES (#{chatRoomId}, #{content}, #{senderId}, #{createdAt}) ")
    @Options(useGeneratedKeys = true, keyProperty = "chatMessageId")
    int insertMessage(ChatMessageDTO chatMessageCreateDTO);

    @Select("SELECT " +
            "room.id AS chatRoomId, " +
            "user2.id AS userId, " +
            "user2.nickname AS userNickname, " +
            "user2.profile_img AS userImage, " +
            "product.id AS productId, " +
            "product.product_images AS productThumbnail, " +
            "room.seller_id AS sellerId " +
            "FROM " +
            "tb_chat_room room " +
            "JOIN tb_chat_mapping mapping ON room.id = mapping.chat_room_id " +
            "JOIN tb_product product ON room.product_id = product.id " +
            "JOIN tb_user user2 ON " +
            "(room.seller_id != #{currentUserId} AND room.seller_id = user2.id) OR " +
            "(mapping.user_id != #{currentUserId} AND mapping.user_id = user2.id) " +
            "WHERE (room.seller_id = #{currentUserId} OR mapping.user_id = #{currentUserId}) " +
            "AND mapping.deleted_at IS NULL")
    List<ChatRoomCardResponseDTO> selectChatRoomsByUserId(@Param("currentUserId") int currentUserId);


    // 유저아이디로 채팅방 목록 조회
//    @Select("""
//            SELECT c.id AS chatRoomId,
//                   p.product_images AS productThumbnail,
//                   p.id AS productId,
//                   CASE WHEN c.seller_id = #{myUserId} THEN TRUE ELSE FALSE END AS isSeller,
//                   u.profile_img AS userImage,
//                   u.name AS userName,
//                   m.content AS recentMessage,
//                   m.created_at AS createAt
//            FROM tb_chat_room AS c
//            LEFT JOIN tb_chat_message AS m ON c.id = m.chat_room_id
//            JOIN tb_chat_mapping AS cm ON cm.chat_room_id = c.id
//            JOIN tb_product AS p ON p.id = c.product_id
//            JOIN tb_user AS u ON u.id = cm.user_id
//            WHERE cm.user_id != #{myUserId} -- 상대방 사용자 정보만 가져오기
//            ORDER BY m.created_at DESC
//            LIMIT 1
//            """)
//    List<ChatRoomCardResponseDTO> selectChatRoomsByUserId(@Param("myUserId") int myUserId);

    @Select("""
    SELECT 
        c.id AS chatRoomId,
        p.product_images AS productThumbnail,
        p.product_name AS productName,
        p.price AS price,
        p.is_negotiable AS isNegotiable,
        p.id AS productId,
        u.id AS userId,
        u.name AS userName,
        u.profile_img AS profileImage,
        rm.id AS chatMessageId,
        rm.content AS content,
        rm.created_at AS createdAt
    FROM tb_chat_room AS c
    JOIN tb_product AS p ON p.id = c.product_id
    JOIN tb_chat_mapping AS cm ON cm.chat_room_id = c.id
    JOIN tb_user AS u ON u.id = cm.user_id
    LEFT JOIN (
        SELECT 
            id,chat_room_id, content, created_at, user_id
        FROM tb_chat_message
        WHERE chat_room_id = #{chatRoomId}
        ORDER BY created_at DESC
        LIMIT 30
    ) rm ON c.id = rm.chat_room_id
    WHERE c.id = #{chatRoomId}
    AND u.id = rm.user_id
    ORDER BY rm.created_at ASC
""")
    List<ChatRoomSQLResultDTO> selectChatRoomDetailsById(@Param("chatRoomId") int chatRoomId);

    @Select("""
    SELECT 
        u.id AS userId,
        u.name AS userName,
        u.nickname AS nickname,
        u.profile_img AS profileImage
    FROM tb_user AS u
    JOIN tb_chat_mapping AS cm ON u.id = cm.user_id
    WHERE cm.chat_room_id = #{chatRoomId}
""")
    List<UserCardDTO> selectParticipantsByChatRoomId(@Param("chatRoomId") int chatRoomId);

    @Select("SELECT EXISTS(SELECT 1 FROM tb_chat_room WHERE id = #{chatRoomId})")
            boolean existsChatRoomById(@Param("chatRoomId") int chatRoomId);

}