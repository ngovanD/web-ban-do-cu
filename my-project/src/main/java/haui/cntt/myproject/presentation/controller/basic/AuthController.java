package haui.cntt.myproject.presentation.controller.basic;

import haui.cntt.myproject.common.exception.BadRequestException;
import haui.cntt.myproject.common.jwt.JwtTokenProvider;
import haui.cntt.myproject.config.UserDetailServiceConfig;
import haui.cntt.myproject.presentation.request.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class AuthController {

    @Autowired
    private UserDetailServiceConfig userDetailServiceConfig;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String home(Model model , @ModelAttribute LoginRequest loginRequest
            , HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            final UserDetails userDetails = userDetailServiceConfig.loadUserByUsername(loginRequest.getUsername());
            authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            String accessToken = jwtTokenProvider.generateAccessToken(userDetails, request);

            Cookie cookie = new Cookie("accessToken", accessToken);
            response.addCookie(cookie);

        }catch (Exception e)
        {
            model.addAttribute("statusLogin", "Kiểm tra lại tài khoản và mật khẩu !!!");
            return "login";
        }

        return "redirect:/home";
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new BadRequestException("Incorrect password.");
        }
    }
}

