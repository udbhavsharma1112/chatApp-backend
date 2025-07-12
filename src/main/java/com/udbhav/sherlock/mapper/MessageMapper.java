package com.udbhav.sherlock.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import com.udbhav.sherlock.model.Message;

public class MessageMapper implements RowMapper<Message> {
    @Override
    public Message map(ResultSet rs, StatementContext ctx) throws SQLException {
        Message message = new Message();
        message.setMessageId(rs.getString("message_id"));
        message.setSenderId(rs.getString("sender_id"));
        message.setReceiverId(rs.getString("receiver_id"));
        message.setMessage(rs.getString("message"));
        return message;
    }


} 