package com.aplus.aplusmarket.config;

import com.aplus.aplusmarket.dto.DataResponseDTO;
import com.aplus.aplusmarket.dto.ResponseDTO;
import com.aplus.aplusmarket.dto.chat.request.ChatRoomCreateDTO;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class ChatAspect {

    private final SimpMessagingTemplate messagingTemplate;

    @AfterReturning(pointcut ="execution(* com.aplus.aplusmarket.controller.chat.ChatController.insertChatRoom(..))", returning = "result")
    public void insertChatRoom(final JoinPoint joinPoint, ResponseDTO result) {
        if (result != null  && result.getCode() == 4000) {
            if (result instanceof DataResponseDTO<?> dataResponse) {
                Object data = dataResponse.getData();
                if (data instanceof ChatRoomCreateDTO) {
                    String destination = "/sub/user/" + ((ChatRoomCreateDTO) data).getUserId();
                    messagingTemplate.convertAndSend(destination, data);
                    System.out.println("Aspect: 웹소켓 메시지 전송 완료 - " + destination + data);
                }
            }
        }


    }

}
