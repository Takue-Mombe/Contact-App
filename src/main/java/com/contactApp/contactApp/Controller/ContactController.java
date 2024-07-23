package com.contactApp.contactApp.Controller;

import com.contactApp.contactApp.Service.ContactService;
import com.contactApp.contactApp.domain.Contact;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("")
public class ContactController {

    private final ContactService contactService1;

    public ContactController(ContactService contactService){
        this.contactService1=contactService;
    }

    @GetMapping("/home")
    public ResponseEntity<Page<Contact>>getContact(
            @RequestParam
    )


}
