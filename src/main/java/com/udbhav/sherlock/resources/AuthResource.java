package com.udbhav.sherlock.resources;

import com.udbhav.sherlock.dao.AuthUserDao;
import com.udbhav.sherlock.dao.UserDao;
import com.udbhav.sherlock.model.AuthUser;
import com.udbhav.sherlock.service.AuthService;
import com.udbhav.sherlock.utils.Logger;

import javax.ws.rs.core.Response;
import java.util.Collections;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    private final AuthService authService;

    public AuthResource(AuthUserDao userDao) {
        this.authService = new AuthService(userDao);
    }

    @POST
    @Path("/register")
    public Response register(AuthUser user) {
        String token = authService.register(user);
        Logger.info("User registered successfully: " + user.getEmailId() + " with token: " + token);
        return Response.ok(Collections.singletonMap("token", token)).build();
    }

    @POST
    @Path("/login")
    public Response login(AuthUser user) {
        Logger.info("Attempting to login user: " + user.getEmailId()+ " with password: " + user.getPassword());
        return authService.login(user.getEmailId(), user.getPassword())
                .map(token -> Response.ok(Collections.singletonMap("token", token)).build())
                .orElse(Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build());
    }
}
