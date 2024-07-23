package com.contactApp.contactApp.Service;


import com.contactApp.contactApp.Repo.ContactRepo;
import com.contactApp.contactApp.domain.Contact;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.contactApp.contactApp.Constant.Constant.PHOTO_DIRECTORY;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepo contactRepo;

    //returns a page of all contacts
    //sorts by name
    public Page<Contact> getAllContacts(int page, int size){
        return contactRepo.findAll(PageRequest.of(page, size, Sort.by("name")));
    }

    //returns a page of one contact
    public Contact getContact(String id){
        return contactRepo.findById(id).orElseThrow(()-> new RuntimeException("Contact not found"));
    }

    //save contact
    public Contact createContact(Contact contact){
        return contactRepo.save(contact);
    }

    //delete Contact
    public void deleteContact(String id){
        contactRepo.deleteById(id);
    }

    // upload pic

    public String uploadPhoto(String id, MultipartFile multipartFile){
        log.info("Saving picture for user ID: {}",id);

        Contact contact=getContact(id);
        String photoUrl=photoFunction.apply(id,multipartFile);

        contact.setPhotoUrl(photoUrl);
        contactRepo.save(contact);

        return photoUrl;


    }

    private final Function<String,String> fileExtension=filename-> Optional.of(filename).filter(name->name.contains("."))
            .map(name->"."+name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoFunction=(id,image)-> {
        String filename=fileExtension.apply(image.getOriginalFilename());
        try {
            Path fileStorage = Paths.get(PHOTO_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(fileStorage)) {Files.createDirectories(fileStorage);}
            Files.copy(image.getInputStream(),fileStorage.resolve(id+ filename),REPLACE_EXISTING);
            return ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/contacts/image"+id+filename).toUriString();
        } catch (Exception e) {
            throw new RuntimeException("Unable  To Save the Image: " + e.getMessage());
        }

    };


}
