package com.oddsocks.elastic.documents;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class DocumentController {
    
    @RequestMapping("/")
    public String index() {
        return "Working";
    }
    
}
