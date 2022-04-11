package haui.cntt.myproject.common.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {
    @ExceptionHandler(RuntimeException.class)
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

    @ExceptionHandler(Exception.class)
    public String checkException_(Exception e)
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
}
