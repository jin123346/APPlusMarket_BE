package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ErrorResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.response.*;
import com.aplus.aplusmarket.entity.ChatMessage;
import com.aplus.aplusmarket.mapper.chat.ChatRoomMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatService {

    final ChatRoomMapper chatRoomMapper;

    public List<ChatMessage> selectAllMessagesByChatRoomId(int chatRoomId) {
        return chatRoomMapper.selectMessagesByChatRoomId(chatRoomId);
    }

    // 유저 아이디로 채팅방 목록 조회 - 2025/02/03 check
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

    public ResponseDTO selectChatRoomDetailsById(int chatRoomId) {

        try {
            if(chatRoomMapper.existsChatRoomById(chatRoomId)) {

                List<ChatRoomSQLResultDTO> chatRoomSQLResult = chatRoomMapper.selectChatRoomDetailsById(chatRoomId);

                ChatRoomDetailDTO chatRoomResponseDTO = toChatRoomDetailDTO(chatRoomSQLResult);

                System.out.println(chatRoomResponseDTO);
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
    public ChatRoomDetailDTO toChatRoomDetailDTO(List<ChatRoomSQLResultDTO> sqlResultList) {
        // 하나의 채팅방만 있다고 가정하므로, 첫 번째 결과만 사용
        if (sqlResultList == null || sqlResultList.isEmpty()) {
            return null;
        }
        // 첫 번째 채팅방 정보를 가져옵니다.
        ChatRoomSQLResultDTO firstResult = sqlResultList.get(0);

        // ChatRoomDetailDTO를 빌더를 이용해 생성
        return ChatRoomDetailDTO.builder()
                .chatRoomId(firstResult.getChatRoomId())
                .productCard(ProductCardDTO.builder()
                        .productId(firstResult.getProductId())
                        .productName(firstResult.getProductName())
                        .thumbnailImage(firstResult.getProductThumbnail())
                        .price(firstResult.getPrice())
                        .isNegotiable(firstResult.getIsNegotiable())
                        .build())
                .participants(
                        sqlResultList.stream()
                                .map(result -> UserCardDTO.builder()
                                        .userId(result.getUserId())
                                        .userName(result.getUserName())
                                        .profileImage(result.getProfileImage())
                                        .build())
                                .distinct() // 중복 유저 제거
                                .limit(2) // 2명만 추출
                                .collect(Collectors.toList()))  // 유저 리스트 그대로 저장
                .messages(sqlResultList.stream()
                        .map(result -> ChatMessageDTO.builder()
                                .chatMessageId(result.getChatMessageId())
                                .userId(result.getUserId())
                                .content(result.getContent())
                                .createdAt(result.getCreateAt())
                                .build())
                        .collect(Collectors.toList())) // 메시지 리스트
                .build();
    }
}

