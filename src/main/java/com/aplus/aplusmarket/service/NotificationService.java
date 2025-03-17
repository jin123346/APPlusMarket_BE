package com.aplus.aplusmarket.service;

import com.aplus.aplusmarket.dto.product.ProductEvent;
import com.aplus.aplusmarket.entity.NotificationItem;
import com.aplus.aplusmarket.mapper.product.NotificationItemMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationItemMapper notificationItemMapper;

    public void sendNotification(Long userId, NotificationItem event){
        String destination = "/topic/notification/" + userId;
        log.info("Sending message to " + destination);
        messagingTemplate.convertAndSend(destination, event);
    }

    public void sendNotificationBatch(List<NotificationItem> event){
        event.stream()
                .collect(Collectors.groupingBy(NotificationItem::getUserId)) // 🔹 유저별 그룹화
                .forEach((userId, userNotifications) -> {
                    String destination = "/topic/notification/" + userId;
                    log.info(" Sending batch notifications to {}", destination);
                    messagingTemplate.convertAndSend(destination, userNotifications); // ✅ 각 유저에게 개별 WebSocket 전송
                });
    }

    public void sendPastNotificationsToWebSocket(long userId) {
        List<NotificationItem> notifications = notificationItemMapper.findByUserIdOrderByTimestampDesc(userId);
        log.info("과거 알림 전송 시작 to web socket {}",userId);

        if (!notifications.isEmpty()) {
            log.info("Sending past notifications to web socket {}",notifications);

            String destination = "/topic/notification/first/" + userId;
            messagingTemplate.convertAndSend(destination, notifications); // 리스트 형태로 전송
            System.out.println("과거 알림 전송 완료: " + notifications.size() + "개");
        } else {
            System.out.println("과거 알림 없음.");
        }

    }

}
