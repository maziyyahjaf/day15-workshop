package sg.edu.nus.iss.day15_workshop.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import sg.edu.nus.iss.day15_workshop.model.Contacts;
import sg.edu.nus.iss.day15_workshop.service.ContactService;


@Controller
@RequestMapping("/tests")
public class TestController {

    private final ContactService contactService;

    
    public TestController(ContactService contactService) {
        this.contactService = contactService;
    }

    @ResponseBody
    @GetMapping("/add")
    public String addContact() throws ParseException {
        DateFormat df = new SimpleDateFormat("dd-MM-yyy");
        Date dob = df.parse("12-12-2016");
        Date dob2 = df.parse("13-12-2016");
        // problem with using Date this way cause of the way Java is handling it
        // via html form is easier
        
      
        return "Person added successfully";
    }

    @ResponseBody
    @GetMapping("/contacts")
    public List<Contacts> contactsFindAll() throws ParseException {
        return contactService.showAllContacts("tests");
    }
    

}
