package com.aplus.aplusmarket.mapper.chat;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
*  2025.02.03 황수빈 - 채팅방 상세정보 조회 메서드 추가
*
*/

@Mapper
public interface ChatRoomMapper {

    /** 메시지 삽입
     * @param chatMessageCreateDTO
     * @return int
     */
    @Insert("INSERT INTO tb_chat_message (chat_room_id, content, user_id, created_at) " +
            "VALUES (#{chatRoomId}, #{content}, #{senderId}, #{createdAt}) ")
    @Options(useGeneratedKeys = true, keyProperty = "chatMessageId")
    int insertMessage(ChatMessageDTO chatMessageCreateDTO);

    @Select("""
        SELECT chat_room_id 
        FROM tb_chat_room cr
        JOIN tb_chat_mapping cm ON cr.id = cm.chat_room_id 
        
        WHERE seller_id = #{sellerId} 
        AND user_id = #{userId}
        AND product_id = #{productId}
   """)
    Integer findMessageIdIfExists (int sellerId, int userId, int productId );


    /** 채팅방 목록 조회
     * @param currentUserId
     * @return
     */
    @Select("""
       SELECT
            room.id AS chatRoomId,
            user2.id AS userId,
            user2.nickname AS userNickname,
            user2.profile_img AS userImage,
            product.id AS productId,
            pi.uuid_name AS productThumbnail,
            room.seller_id AS sellerId
        FROM
            tb_chat_room room
        JOIN
            tb_chat_mapping mapping ON room.id = mapping.chat_room_id
      LEFT JOIN (
                  SELECT
                      id,chat_room_id, content, created_at, user_id
                  FROM tb_chat_message
            
                  ORDER BY created_at DESC
                  LIMIT 1
              ) rm ON room.id = rm.chat_room_id
        JOIN
            tb_product product ON room.product_id = product.id
        JOIN
            tb_product_image pi on product.id =pi.product_id
        JOIN
            tb_user user2 ON
            (room.seller_id != #{currentUserId} AND room.seller_id = user2.id) OR
            (mapping.user_id != #{currentUserId} AND mapping.user_id = user2.id)
        WHERE
            (room.seller_id = #{currentUserId} OR mapping.user_id = #{currentUserId})
            AND mapping.deleted_at IS NULL
            AND pi.sequence = 0;
        

    """)
    List<ChatRoomCardResponseDTO> selectChatRoomsByUserId(@Param("currentUserId") int currentUserId);

    // TODO : (중요) 메시지 조회 *
    /** 채팅방 조회 - 최근 메시지 30개
     * @param chatRoomId
     * @return
     */
    @Select("""
        SELECT
            c.id AS chatRoomId,
            tpi.uuid_name AS productThumbnail,
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
        JOIN tb_product_image tpi ON tpi.product_id = p.id AND tpi.sequence = 0
        LEFT JOIN (
            SELECT
                id, chat_room_id, content, created_at, user_id
            FROM tb_chat_message
            WHERE chat_room_id = #{chatRoomId}
            ORDER BY created_at DESC
            LIMIT 30
        ) rm ON c.id = rm.chat_room_id AND u.id = rm.user_id
        WHERE c.id = #{chatRoomId}
        ORDER BY rm.created_at ASC;
        """)
    List<ChatRoomSQLResultDTO> selectChatRoomDetailsById(@Param("chatRoomId") int chatRoomId);

    /** 채팅방 참가자 조회
     * @param chatRoomId
     * @return
     */
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

    /** 채팅방 존재여부 확인
     * @param chatRoomId
     * @return
     */
    @Select("SELECT EXISTS(SELECT 1 FROM tb_chat_room WHERE id = #{chatRoomId})")
            boolean existsChatRoomById(@Param("chatRoomId") int chatRoomId);

    /** 구독할 채팅 목록 조회
     * @param userID
     * @return
     */
    @Select("SELECT chat_room_id FROM tb_chat_mapping WHERE user_id = #{userID}")
    List<Integer> selectChatIdByUserId(@Param("userID") int userID);

    /** 채팅방 생성
     * @param chatRoom
     * @return
     */
    @Insert("""
        INSERT INTO tb_chat_room (product_id, seller_id, created_at)
        VALUES (#{productId}, #{sellerId}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "chatRoomId")
    int insertChatRoom(ChatRoomCreateDTO chatRoom);

    /** 채팅 매핑 생성
     * @param chatRoomId
     * @param userId
     * @return
     */
    @Insert("""
        INSERT INTO tb_chat_mapping (chat_room_id, user_id)
        VALUES (#{chatRoomId}, #{userId})
    """)
    int insertChatMapping(int chatRoomId, int userId);
}