package com.udbhav.sherlock.service;

import java.util.UUID;

import com.udbhav.sherlock.dao.MessageDao;
import com.udbhav.sherlock.dao.UserChatDao;
import com.udbhav.sherlock.model.Message;

public class MessageService {
    private final UserChatDao userChatDao;
    private final MessageDao messageDao;

    public MessageService(UserChatDao userChatDao, MessageDao messageDao){
        this.messageDao = messageDao;
        this.userChatDao = userChatDao;
    }
    public void saveIncommingMessages(Message message){
        String messageId = UUID.randomUUID().toString();
        messageDao.insertMessage(messageId, message.getSenderId(), message.getReceiverId(), message.getMessage());
        String chatMappingId = UUID.randomUUID().toString();
        userChatDao.insertChatUserMapping(chatMappingId,message.getSenderId(),message.getReceiverId());
    }   
}
