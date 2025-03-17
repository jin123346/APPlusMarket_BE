package com.aplus.aplusmarket.controller;

import com.aplus.aplusmarket.dto.product.ProductEvent;
import com.aplus.aplusmarket.entity.NotificationItem;
import com.aplus.aplusmarket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MessageController {

    private final NotificationService notificationService;

//    @MessageMapping("/notifications/history")
//    public void sendPastNotifications(NotificationItem event) {
//
//        notificationService.sendPastNotificationsToWebSocket(event.getUserId());
//    }
}
