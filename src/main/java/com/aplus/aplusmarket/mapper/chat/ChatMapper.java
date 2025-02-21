package com.aplus.aplusmarket.mapper.chat;
import com.aplus.aplusmarket.dto.chat.UserCardDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageCreateDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

/*
*  2025.02.03 황수빈 - 채팅방 상세정보 조회 메서드 추가
*
*/

@Mapper
public interface ChatMapper {


    // Select

    /** 채팅방 목록 조회
     * @param currentUserId
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
    List<ChatRoomCardResponseDTO> selectChatRoomsByUid(@Param("currentUserId") int currentUserId);

    /** 구독할 채팅 목록 조회
     * @param userID
     */
    @Select("SELECT chat_room_id FROM tb_chat_mapping WHERE user_id = #{userID}")
    List<Integer> selectChatIdByUserId(@Param("userID") int userID);


    /** 채팅방 정보 조회 - 상품
     * @param chatRoomId
     */
    @Select("""
                 select
                       c.id AS chatRoomId,
                       pi.uuid_name AS productThumbnail,
                       p.product_name AS productName,
                       p.price AS price,
                       p.is_negotiable AS isNegotiable,
                       p.id AS productId           
               from tb_chat_room c
                 join tb_product p on p.id = c.product_id
                 join tb_product_image pi on pi.product_id = p.id
                 where pi.`sequence` = 0
                 and c.id = #{chatRoomId}
    """)
    ChatRoomSQLResultDTO selectChatRoomInfo(@Param("chatRoomId")int chatRoomId);

    /**  채팅방 정보 조회 - 참가자
     * @param chatRoomId
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

    /** 채팅방 존재 여부 확인
     * @param chatRoomId
     */
    @Select("SELECT EXISTS(SELECT 1 FROM tb_chat_room WHERE id = #{chatRoomId})")
            boolean existsChatRoomById(@Param("chatRoomId") int chatRoomId);

    /** 생성 전 채팅 존재 여부 확인
     * @param sellerId, userId, productId
     * @return chatRoomId
     */

    @Select("""
        SELECT chat_room_id AS chatRoomId,
               seller_id AS sellerId,
               product_id AS productId,
               user_id AS userId,
               created_at AS createdAt
        FROM tb_chat_room cr
        JOIN tb_chat_mapping cm ON cr.id = cm.chat_room_id 
        WHERE seller_id = #{sellerId} 
        AND user_id = #{userId}
        AND product_id = #{productId}
   """)
    ChatRoomCreateDTO findChatRoomIdIfExists (int sellerId, int userId, int productId );





    // Insert

    /**
     * 채팅방 생성
     * @param chatRoom
     */
    @Insert("""
        INSERT INTO tb_chat_room (product_id, seller_id, created_at)
        VALUES (#{productId}, #{sellerId}, #{createdAt})
    """)
    @Options(useGeneratedKeys = true, keyProperty = "chatRoomId")
    void insertChatRoom(ChatRoomCreateDTO chatRoom);

    /**
     * 채팅 매핑 생성
     * @param userId, chatRoomId
     */
    @Insert("""
        INSERT INTO tb_chat_mapping (chat_room_id, user_id)
        VALUES (#{chatRoomId}, #{userId})
    """)
    void insertChatMapping(int chatRoomId, int userId);

    /** 메시지 삽입
     * @param chatMessageCreateDTO
     * @return chatMessageId
     */
    @Insert("INSERT INTO tb_chat_message (chat_room_id, content, user_id, created_at) " +
            "VALUES (#{chatRoomId}, #{content}, #{senderId}, #{createdAt}) ")
    @Options(useGeneratedKeys = true, keyProperty = "chatMessageId")
    int insertMessage(ChatMessageCreateDTO chatMessageCreateDTO);


}