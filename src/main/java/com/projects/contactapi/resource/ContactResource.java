package com.projects.contactapi.resource;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

import com.projects.contactapi.constant.Constant;
import com.projects.contactapi.entity.Contact;
import com.projects.contactapi.service.ContactService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactResource {

	private final ContactService contactService;

	@PostMapping
	public ResponseEntity<Contact> createContact(@RequestBody Contact contact) {

		return ResponseEntity.created(URI.create("/contacts/userID")).body(contactService.createContact(contact));
	}
	
	
	   @GetMapping
	    public ResponseEntity<Page<Contact>> getContacts(@RequestParam(value = "page", defaultValue = "0") int page,
	                                                     @RequestParam(value = "size", defaultValue = "10") int size) {
	        return ResponseEntity.ok().body(contactService.getAllContacts(page, size));
	    }
	
	@GetMapping("/{id}")
	public ResponseEntity<Contact> getContact(@PathVariable(value = "id") String id){
		return ResponseEntity.ok().body(contactService.getContact(id));
	}
	
	 @DeleteMapping("/{id}")
	    public ResponseEntity<Void> deleteContact(@PathVariable("id") String id) {
	        contactService.deleteContact(id);
	        return ResponseEntity.noContent().build(); // Returns HTTP 204 No Content
	    }
	
	@PutMapping("/photo")
	public ResponseEntity<String> uploadPhoto(@RequestParam(value = "id") String id, @RequestParam("file") MultipartFile file){
		
		return ResponseEntity.ok().body(contactService.uploadPhoto(id, file));
	}

	@GetMapping(path = "/image/{filename}", produces = {IMAGE_PNG_VALUE, IMAGE_JPEG_VALUE })
    public byte[] getPhoto(@PathVariable("filename") String filename) throws IOException {
        return Files.readAllBytes(Paths.get(Constant.PHOTO_DIRECTORY + filename));
    }
}