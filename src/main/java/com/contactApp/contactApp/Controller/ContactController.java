package com.contactApp.contactApp.Controller;

import com.contactApp.contactApp.Service.ContactService;
import com.contactApp.contactApp.domain.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.contactApp.contactApp.Constant.Constant.PHOTO_DIRECTORY;
import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@Controller
@RequiredArgsConstructor
@RequestMapping("contacts/")
public class ContactController {

    private final ContactService contactService1;

    @PostMapping
    public ResponseEntity<Contact>saveContract(@RequestBody Contact contact){
     //   return ResponseEntity.ok().body(contactService1.createContact(contact));
        return ResponseEntity.created(URI.create("contacts/userId")).body(contactService1.createContact(contact));
    }

    @GetMapping
    public ResponseEntity<Page<Contact>>getContact(
            @RequestParam(value="page",defaultValue = "0")int page,
            @RequestParam(value = "size",defaultValue = "10")int size
    ){
        return ResponseEntity.ok().body(contactService1.getAllContacts(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact>getContacts(@PathVariable (value="id") String id){
        return ResponseEntity.ok().body(contactService1.getContact(id));
    }

    @PutMapping("/photo")
    public ResponseEntity<String>uploadPhoto(@RequestParam("id") String id, @RequestParam("file")MultipartFile multipartFile){
        return ResponseEntity.ok().body(contactService1.uploadPhoto(id,multipartFile));
    }

    @GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE})
    public byte[] getPhoto(@PathVariable("filename")String filename)throws Exception{
        return Files.readAllBytes((Paths.get(PHOTO_DIRECTORY+filename)));
    }





}
