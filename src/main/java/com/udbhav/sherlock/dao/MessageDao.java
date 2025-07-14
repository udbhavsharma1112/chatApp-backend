package com.udbhav.sherlock.dao;

import com.udbhav.sherlock.model.Message;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface MessageDao {

    @SqlUpdate("INSERT INTO messages (message_id, sender_id, receiver_id, message) " +
            "VALUES (:messageId, :senderId, :receiverId, :message)")
    void insertMessage(
            @Bind("messageId") String messageId,
            @Bind("senderId") String senderId,
            @Bind("receiverId") String receiverId,
            @Bind("message") String message);

    @SqlQuery("SELECT * FROM messages " +
            "WHERE (sender_id = :user1 AND receiver_id = :user2) " +
            "   OR (sender_id = :user2 AND receiver_id = :user1) " +
            "ORDER BY created_at")
    List<Message> getMessagesBetweenUsers(@Bind("user1") String user1, @Bind("user2") String user2);

//     @SqlQuery("INSERT INTO messages (message_id, sender_id, )")

    
}
