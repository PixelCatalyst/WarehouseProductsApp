package com.pixcat.warehouseproducts.exception;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class ResponseStatusExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            Object handler,
            @NotNull Exception toBeResolved) {

        try {
            if (toBeResolved instanceof IllegalArgumentException) {
                response.sendError(HttpStatus.BAD_REQUEST.value());
            }
        } catch (IOException ex) {
            log.warn("Unable to resolve exception", ex);
        }
        return new ModelAndView();
    }
}
