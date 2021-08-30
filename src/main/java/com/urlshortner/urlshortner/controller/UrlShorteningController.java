package com.urlshortner.urlshortner.controller;

import com.google.common.base.Strings;
import com.urlshortner.urlshortner.Request.UrlRequest;
import com.urlshortner.urlshortner.Response.ErrorResponse;
import com.urlshortner.urlshortner.Response.UrlResponse;
import com.urlshortner.urlshortner.model.Url;
import com.urlshortner.urlshortner.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin(origins = {"https://shorttly.herokuapp.com/", "http://shorttly.herokuapp.com/"})
public class UrlShorteningController {

    @Autowired
    private UrlService urlService;

    @GetMapping("/")
    @ResponseBody
    public ModelAndView indexPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }

    //generate the short url
    @PostMapping("/generate")
    public ResponseEntity<?> generate(@RequestBody UrlRequest urlRequest){
        Url url = urlService.generateShortLink(urlRequest);
        if(url!=null){
            UrlResponse response = new UrlResponse();
            response.setOriginalUrl(url.getOriginalUrl());
            response.setShortLink(url.getShortLink());
            response.setExpirationDate(url.getExpirationDate());

            return new ResponseEntity<UrlResponse>(response, HttpStatus.OK);

        }
        //error
        ErrorResponse error = new ErrorResponse();
        error.setError("There was an error processing your request. please try again.");
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
        return new ResponseEntity<ErrorResponse>(error, HttpStatus.OK);
    }

    //redirect to the original url
    @GetMapping("/{id}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String id,HttpServletResponse response) throws IOException {
        String original = urlService.redirectToOriginalUrl(id);
        if(original==null){
            throw new RuntimeException("Url does not exist or it might have expired!");
        }
        if(original.equals("expired")) {
            throw new RuntimeException("Url Expired. Please try generating a fresh one.");
        }
        response.sendRedirect(original);
        return new ResponseEntity<>("Redirected To : "+ original,HttpStatus.OK);
    }


}
