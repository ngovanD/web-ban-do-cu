package haui.cntt.myproject.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    public String checkException(RuntimeException e)
    {
        if(e instanceof BadRequestException)
        {
            return e.getMessage();
        }
        else if(e instanceof UnauthorizedException)
        {
            return "redirect:/login";
        }

        return e.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public String checkException2(Exception e)
    {
        return e.getMessage();
    }
}
