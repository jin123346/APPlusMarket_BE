package com.aplus.aplusmarket.mapper.product;

import com.aplus.aplusmarket.entity.NotificationItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationItemMapper {
    NotificationItem insertNotificationItem(NotificationItem notificationItem);

    List<NotificationItem>  findByUserIdOrderByTimestampDesc(@Param("userId") long  userId);
}
