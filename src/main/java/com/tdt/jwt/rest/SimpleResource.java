package com.tdt.jwt.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 **/
@RestController
public class SimpleResource {

    @RequestMapping("/hello")
    @ResponseBody String hello() {
        return "Hello";
    }
}
