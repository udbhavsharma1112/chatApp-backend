package com.udbhav.sherlock.resources;

// import java.net.http.HttpHeaders;
import javax.ws.rs.core.HttpHeaders;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.udbhav.sherlock.dao.UserChatDao;
import com.udbhav.sherlock.dao.UserDao;
import com.udbhav.sherlock.service.UserChatService;
import com.udbhav.sherlock.utils.AuthUtil;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserChatHistoryResource {
    private final UserChatService userChatService;

    public UserChatHistoryResource(UserChatDao userChatDao, UserDao userDao) {
        this.userChatService = new UserChatService(userChatDao, userDao);
    }

    @GET
    @Path("/chat_list")
    public Response getChatList(@Context HttpHeaders headers) {
        try {
            // Extract JWT token from Authorization header
            String authHeader = headers.getHeaderString("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Authorization header must be provided with Bearer token")
                        .build();
            }
            String token = authHeader.substring("Bearer ".length()).trim();

            // Validate token and extract userId (depends on your AuthService)
            String userId = AuthUtil.validateTokenAndGetUserId(token);

            if (userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid or expired token")
                        .build();
            }
            // Fetch chat list for the user
            var chatList = userChatService.getUserChatListHistory(userId);
            return Response.ok(chatList).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Server error: " + e.getMessage())
                    .build();
        }
    }

}
