package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import com.aplus.aplusmarket.mapper.chat.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

    final ChatRoomMapper chatRoomMapper;
    final SimpMessagingTemplate messagingTemplate;

    /** 메시지 Insert
     * (웹소켓을 이용함으로 ResponseDTO로 내보내지 않음)
     * @param chatMessage
     * @return ChatMessageDTO
     */
    public ChatMessageDTO insertMessage(ChatMessageDTO chatMessage) {
        try {
            chatMessage.setCreatedAt(LocalDateTime.now()); // LocalDateTime 그대로 저장
            log.info("메시지 저장 요청: {}", chatMessage); // 로그 레벨 변경

            int result = chatRoomMapper.insertMessage(chatMessage);
            if (result > 0) {
                return chatMessage;
            } else {
                throw new RuntimeException("채팅 메시지 삽입 실패: DB에 저장되지 않음");
            }
        } catch (Exception e) {
            log.error("채팅 메시지 삽입 중 오류 발생: ", e);
            throw new RuntimeException("Insert message failed", e); // 원래 예외 포함하여 던지기
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
            return ErrorResponseDTO.of(5000, "채팅방 목록 조회 실패 : " + e.getMessage());
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
                    .status("failed")
                    .code(5000)
                    .message("해당 id 채팅방이 존재하지 않습니다.")
                    .build();

        }
        catch (Exception e) {
            log.error(e);
            return ErrorResponseDTO.of(5000, "채팅방 상세 조회 실패 : " + e.getMessage());
        }
    }

    /** SQL 문의 결과를 ChatRoomDetailDTO 로 매핑
     * @param sqlResultList
     * @param participants
     * @return ChatRoomDetailDTO
     */
    private ChatRoomDetailDTO toChatRoomDetailDTO(List<ChatRoomSQLResultDTO> sqlResultList, List<UserCardDTO> participants) throws Exception {
        if (sqlResultList == null || sqlResultList.isEmpty()) {
            throw new Exception("해당하는 아이디로 조회되는 채팅이 없으므로 매핑할 수 없습니다.");
        }

        ChatRoomSQLResultDTO firstResult = sqlResultList.get(0);

        try {
            List<ChatMessageDTO> messages = sqlResultList.stream()
                    .filter(result -> result.getChatMessageId() > 0) // 메시지가 있는 경우만 변환
                    .map(result -> ChatMessageDTO.builder()
                            .chatMessageId(result.getChatMessageId())
                            .senderId(result.getUserId())
                            .content(result.getContent())
                            .createdAt(result.getCreatedAt())
                            .build())
                    .collect(Collectors.toList());

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
                    .messages(messages.isEmpty() ? null : messages) // 메시지가 없으면 null
                    .build();
        } catch (Exception e) {
            log.error("ChatRoomDetailDTO 매핑 중 오류 발생", e);
            throw e;
        }
    }


    /** userId로 구독할 채팅방 id 조회
     * @param userId
     * @return List<int>
     */
    public ResponseDTO selectChatRoomIdsByUserId(int userId) {
        List<Integer> result;
        try {
            result = chatRoomMapper.selectChatIdByUserId(userId);
            if (result == null || result.isEmpty()) {
                return DataResponseDTO.of(Collections.emptyList(), 4001, "해당하는 Id로 조회되는 채팅방이 없습니다.");
            }

            return DataResponseDTO.of(result, 4000, "채팅방 아이디 목록 조회 성공");
        } catch (Exception e) {

            log.error("채팅방 ID 조회 중 오류 발생: ", e);
            return ErrorResponseDTO.of(5000, "서버 오류로 인해 채팅방 목록을 가져올 수 없습니다.");
        }
    }

    /** 채팅방 생성
     * @param chatRoom
     * @return
     */
    @Transactional
    public ResponseDTO insertChatRoom(ChatRoomCreateDTO chatRoom){
        try {
            Integer idIfExists = chatRoomMapper.findMessageIdIfExists(chatRoom.getSellerId(), chatRoom.getUserId(), chatRoom.getProductId());
            if(idIfExists != null){
                return DataResponseDTO.of(idIfExists, 4000 ,"이미 존재하던 채팅방을 전송하였습니다.");
            }
            chatRoom.setCreatedAt(LocalDateTime.now());
             chatRoomMapper.insertChatRoom(chatRoom); // 저장된 이후 DTO에 id 값 가지고 나옴
            if(chatRoomMapper.existsChatRoomById(chatRoom.getChatRoomId())) {
                chatRoomMapper.insertChatMapping(chatRoom.getChatRoomId(), chatRoom.getUserId());
                chatRoomMapper.insertChatMapping(chatRoom.getChatRoomId(), chatRoom.getSellerId());
                return DataResponseDTO.of(chatRoom.getChatRoomId(),4000, "채팅방 생성 성공");
            }else {
                log.error("저장된 채팅방 id를 조회하지 못하였습니다");
                return ErrorResponseDTO.of(5000,"저장된 채팅방 id를 조회하지 못하였습니다.");
            }

        }catch (Exception e){
            log.error(e);
            return ErrorResponseDTO.of(5000,"채팅방 생성 중 오류가 발생하였습니다.");
        }
    }
}

