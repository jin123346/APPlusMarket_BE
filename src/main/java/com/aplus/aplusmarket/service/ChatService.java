package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import com.aplus.aplusmarket.mapper.chat.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

    final ChatRoomMapper chatRoomMapper;
    final SimpMessagingTemplate messagingTemplate;

    /** 메시지 Insert
     * @param chatMessage
     * @return ChatMessageDTO
     */
    public ChatMessageDTO insertMessage(ChatMessageDTO chatMessage) {
        try {

            chatMessage.setCreatedAt(LocalDateTime.now().toString());
            log.error(chatMessage);
            int result = chatRoomMapper.insertMessage(chatMessage);
            if(result > 0){
                return chatMessage;
            }else {
                throw new RuntimeException("");
            }
        }
        catch (Exception e) {
            log.error(e);
            throw new RuntimeException("Insert message failed");
        }
    }

    /** user_id로 채팅방 목록 조회
     * @param userId
     * @return
     */
    public ResponseDTO selectChatRoomsByUserId(int userId) {
        try {
            log.info("채팅방 조회 요청 userId : {}", userId);
            List<ChatRoomCardResponseDTO> chatRooms = chatRoomMapper.selectChatRoomsByUserId(userId);

            if (chatRooms == null || chatRooms.isEmpty()) {
                return ResponseDTO.builder()
                        .status("success")
                        .code(4000)
                        .message("해당 유저의 채팅방은 존재하지 않습니다.")
                        .build();
            }

            return new DataResponseDTO<>(chatRooms, 4000, "채팅방 목록 조회 성공");

        } catch (Exception e) {
            log.error(e);
            return ErrorResponseDTO.of(4001, "채팅방 목록 조회 실패 : " + e.getMessage());
        }


    }

    /** id로 채팅방 상세 조회
     * @param chatRoomId
     * @return ChatMessageDTO
     */
    @Transactional
    public ResponseDTO selectChatRoomDetailsById(int chatRoomId) {

        try {
            if(chatRoomMapper.existsChatRoomById(chatRoomId)) {

                List<ChatRoomSQLResultDTO> chatRoomSQLResult = chatRoomMapper.selectChatRoomDetailsById(chatRoomId);
                log.error("💣 최종 chatRoomSQLResult: {}", chatRoomSQLResult);

                List<UserCardDTO> participants = chatRoomMapper.selectParticipantsByChatRoomId(chatRoomId);
                ChatRoomDetailDTO chatRoomResponseDTO = toChatRoomDetailDTO(chatRoomSQLResult,participants);

                log.error("💣 최종 결과값: {}", chatRoomResponseDTO);
                return new DataResponseDTO<>(chatRoomResponseDTO, 4000, "채팅방 상세 조회 성공");
            }
            return ResponseDTO.builder()
                    .code(4001)
                    .message("해당 유저의 채팅방이 존재하지 않습니다.")
                    .build();

        }
        catch (Exception e) {
            log.error(e);
            return ErrorResponseDTO.of(4001, "채팅방 상세 조회 실패 : " + e.getMessage());
        }
    }


    /** SQL 문의 결과를 ChatRoomDetailDTO 로 매핑
     * @param sqlResultList
     * @param participants
     * @return ChatRoomDetailDTO
     */
    private ChatRoomDetailDTO toChatRoomDetailDTO(List<ChatRoomSQLResultDTO> sqlResultList,List<UserCardDTO> participants) throws Exception {


        if (sqlResultList == null || sqlResultList.isEmpty()) {
            throw new Exception("해당하는 아이디로 조회되는 채팅이 없습니다");
        }

        ChatRoomSQLResultDTO firstResult = sqlResultList.get(0);

        try{
            return ChatRoomDetailDTO.builder()
                    .chatRoomId(firstResult.getChatRoomId())
                    .productCard(ProductCardDTO.builder()
                            .productId(firstResult.getProductId())
                            .productName(firstResult.getProductName())
                            .thumbnailImage(firstResult.getProductThumbnail())
                            .price(firstResult.getPrice())
                            .isNegotiable(firstResult.getIsNegotiable())
                            .build())
                    .participants(participants)
                    .messages(sqlResultList.stream()
                            .map(result -> ChatMessageDTO.builder()
                                    .chatMessageId(result.getChatMessageId())
                                    .senderId(result.getUserId())
                                    .content(result.getContent())
                                    .createdAt(result.getCreatedAt())
                                    .build())
                            .collect(Collectors.toList())) // 메시지 리스트
                    .build();
        }catch(Exception e) {
            log.error(e);
            throw e;
        }
    }

    public ResponseDTO selectChatRoomIdsByUserId(int userId) {
        List<Integer> result = null;
        try {
           result = chatRoomMapper.selectChatIdByUserId(userId);
        }catch (Exception e) {
            log.error(e);
            return DataResponseDTO.of(result,4001,"해당하는 Id로 조회되는 채팅방이 없습니다.");
        }
        return DataResponseDTO.of(result, 4000, "채팅방 아이디 목록 조회 성공");
    }
}

