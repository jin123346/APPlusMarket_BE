package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.documents.ChatMessage;
import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.ProductCardDTO;
import com.aplus.aplusmarket.dto.chat.UserCardDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatMessageDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import com.aplus.aplusmarket.mapper.chat.ChatMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * packageName    : com/aplus/aplusmarket/service/ChatService.java
 * fileName       : ChatService.java
 * author         : 황수빈
 * date           : 2024/01/26
 * description    : Chat 기능 Service
 *
 * =============================================================
 * DATE           AUTHOR             NOTE
 * -------------------------------------------------------------
 * 2025.01.26     황수빈     채팅 서비스 생성
 * 2025.02.07     황수빈     채팅방 목록, 상세 조회 메서드 추가
 * 2025.02.14     황수빈     채팅방 생성 메서드 추가
 */

@Log4j2
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMapper chatMapper;
    private final ChatMessageService chatMessageService;

    /** 채팅방 목록 조회
     * @param userId
     * @return List<ChatRoomCardResponseDTO>
     */
    public ResponseDTO selectChatRoomsByUid(int userId) {
        try {
            List<ChatRoomCardResponseDTO> chatRooms = chatMapper.selectChatRoomsByUid(userId);

            if (chatRooms == null || chatRooms.isEmpty()) {
                return ResponseDTO.builder()
                        .status("success")
                        .code(4000)
                        .message("해당 유저의 채팅방은 존재하지 않습니다.")
                        .build();
            }

            // 최신 메시지가 있는 채팅방만 담을 리스트 생성
            List<ChatRoomCardResponseDTO> filteredChatRooms = new ArrayList<>();
            for (ChatRoomCardResponseDTO chatRoom : chatRooms) {
                try {
                    ChatMessage chatMessage = chatMessageService.getRecentMessageByChatRoomId(chatRoom.getChatRoomId());
                    // 최신 메시지 존재 시 필드 업데이트 및 리스트 추가
                    chatRoom.setRecentMessage(chatMessage.getContent());
                    chatRoom.setMessageCreatedAt(chatMessage.getCreatedAt().toString());
                    filteredChatRooms.add(chatRoom);
                } catch (Exception e) {
                    // 최신 메시지가 없어서 예외 발생 시, 해당 채팅방은 건너뜁니다.
                    log.info("채팅방 {} 최신 메시지 없음: {}", chatRoom.getChatRoomId(), e.getMessage());
                }
            }

            if (filteredChatRooms.isEmpty()) {
                return ResponseDTO.builder()
                        .status("success")
                        .code(4000)
                        .message("최신 메시지가 있는 채팅방이 존재하지 않습니다.")
                        .build();
            }

            return new DataResponseDTO<>(filteredChatRooms, 4000, "채팅방 목록 조회 성공");

        } catch (Exception e) {
            log.error(e);
            return ErrorResponseDTO.of(5000, "채팅방 목록 조회 실패 : " + e.getMessage());
        }
    }

    /** 채팅방 상세 조회
     * @param chatRoomId
     * @return ChatMessageDTO
     */
    @Transactional
    public ResponseDTO selectChatRoomInfo(int chatRoomId) {
        try {
            if(chatMapper.existsChatRoomById(chatRoomId)) {

                ChatRoomSQLResultDTO SqlResult = chatMapper.selectChatRoomInfo(chatRoomId);
                List<UserCardDTO> participants = chatMapper.selectParticipantsByChatRoomId(chatRoomId);
                List<ChatMessageDTO> messages = chatMessageService.getChatMessages(chatRoomId);

                ProductCardDTO productCard = ProductCardDTO.builder()
                        .productId(SqlResult.getProductId())
                        .productName(SqlResult.getProductName())
                        .thumbnailImage(SqlResult.getProductThumbnail())
                        .isNegotiable(SqlResult.getIsNegotiable())
                        .price(SqlResult.getPrice()).build();

                ChatRoomDetailDTO result = ChatRoomDetailDTO.builder()
                        .chatRoomId(chatRoomId)
                        .productCard(productCard)
                        .participants(participants)
                        .messages(messages)
                        .build();
                return new DataResponseDTO<>(result, 4000, "채팅방 상세 조회 성공");
            }
            return ResponseDTO.builder()
                    .status("failed")
                    .code(5000)
                    .message("해당 id 채팅방이 존재하지 않습니다.")
                    .build();

        }
        catch (Exception e) {
            return ErrorResponseDTO.of(5000, "채팅방 상세 조회 실패 : " + e.getMessage());
        }
    }

    /** 구독할 채팅방 id 조회
     * @param userId
     * @return List<int>
     */
    public ResponseDTO selectChatRoomIdsByUserId(int userId) {
        List<Integer> result;
        try {
            result = chatMapper.selectChatIdByUserId(userId);
            if (result == null || result.isEmpty()) {
                return DataResponseDTO.of(Collections.emptyList(), 4001, "해당하는 Id로 조회되는 채팅방이 없습니다.");
            }

            return DataResponseDTO.of(result, 4000, "채팅방 아이디 목록 조회 성공");
        } catch (Exception e) {

            log.error("채팅방 ID 조회 중 오류 발생: ", e);
            return ErrorResponseDTO.of(5000, "서버 오류로 인해 채팅방 목록을 가져올 수 없습니다.");
        }
    }

    // insert

    /** 채팅방 생성
     * @param chatRoom
     * @return chatRoomId (int)
     */
    @Transactional
    public ResponseDTO insertChatRoom(ChatRoomCreateDTO chatRoom){
        try {
            ChatRoomCreateDTO idIfExists = chatMapper.findChatRoomIdIfExists(chatRoom.getSellerId(), chatRoom.getUserId(), chatRoom.getProductId());
            if(idIfExists != null){
                return DataResponseDTO.of(idIfExists, 4000 ,"이미 존재하던 채팅방을 전송하였습니다.");
            }
            chatRoom.setCreatedAt(LocalDateTime.now());
             chatMapper.insertChatRoom(chatRoom);

             // 저장된 이후 DTO에 id 값 가지고 나옴
            if(chatMapper.existsChatRoomById(chatRoom.getChatRoomId())) {

                chatMapper.insertChatMapping(chatRoom.getChatRoomId(), chatRoom.getUserId());
                chatMapper.insertChatMapping(chatRoom.getChatRoomId(), chatRoom.getSellerId());

                return DataResponseDTO.of(chatRoom,4000, "채팅방 생성 성공");
            }else {
                log.error("저장된 채팅방 id를 조회하지 못하였습니다");
                return ErrorResponseDTO.of(5000,"저장된 채팅방 id를 조회하지 못하였습니다.");
            }

        }catch (Exception e){
            log.error(e);
            return ErrorResponseDTO.of(5000,"채팅방 생성 중 오류가 발생하였습니다.");
        }
    }

    // update

    // update
    @Transactional
    public ResponseDTO updateAppointment(ChatMessageDTO chatMessage) {
        try {
            // 메시지 Id check
            if (chatMessage.getMessageId() == null || chatMessage.getMessageId().isEmpty()) {
                return ErrorResponseDTO.of(4001, "메시지 ID는 필수입니다.");
            }

           ChatMessage updatedMessage = chatMessageService.updateAppointment(chatMessage);

            if (updatedMessage != null) {
                return ResponseDTO.builder()
                        .status("success")
                        .code(4000)
                        .message("Appointment 업데이트 성공")
                        .build();
            } else {
                return ErrorResponseDTO.of(4002, "업데이트할 데이터가 없습니다.");
            }

        } catch (Exception e) {
            log.error("updateAppointment 오류: ", e);
            return ErrorResponseDTO.of(5000, "서버 오류가 발생했습니다.");
        }
    }


}

