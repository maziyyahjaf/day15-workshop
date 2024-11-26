package sg.edu.nus.iss.day15_workshop.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import sg.edu.nus.iss.day15_workshop.model.Contacts;
import sg.edu.nus.iss.day15_workshop.service.ContactService;
import sg.edu.nus.iss.day15_workshop.util.Util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("")
    public String showAllContacts(Model model) throws ParseException {
        List<Contacts> contacts = contactService.showAllContacts(Util.keyContacts);
        System.out.println("From contacts service: " + contacts);
        model.addAttribute("contacts", contacts);
        return "showAllContacts";
    }

    @GetMapping("/add")
    public String showAddContactForm(Model model) {

        Contacts contact = new Contacts();
        model.addAttribute("contact", contact);
        return "addContact";
    }

    @PostMapping("/add")
    public String postAddContact(@Valid @ModelAttribute("contact") Contacts contact,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "addContact";
        }

        // custom validation/error handling
        // cannot be younger than 10 years old and older than 100 years old
        LocalDate currentDate = LocalDate.now();

        // Calculate the age
        int age = Period.between(contact.getDateOfBirth(), currentDate).getYears();

        // Check age constraints
        if (age < 10 || age > 100) {
            FieldError err = new FieldError("contact", "dateOfBirth",
                    "Age must be between 10 and 100 years old");
            result.addError(err);
            return "addContact";
        }

        // if you don't do this id, hexId will be null
        Contacts c = new Contacts(contact.getName(), contact.getEmail(), contact.getPhoneNumber(),
                contact.getDateOfBirth(), contact.getPostalCode());

        contactService.addContact(Util.keyContacts, c);

        return "redirect:/contacts";
    }

    @GetMapping("/delete/{hex-id}")
    public String deleteContact(@PathVariable("hex-id") String contactHexId, Model model) throws ParseException {

        List<Contacts> contacts = contactService.showAllContacts(Util.keyContacts);
        Contacts contactToDelete = contacts.stream()
                .filter(contact -> contact.getHexId().equals(contactHexId))
                .findFirst()
                .get();

        contactService.deleteContact(Util.keyContacts, contactToDelete);

        return "redirect:/contacts";
    }

    @GetMapping("/edit/{hex-id}")
    public String showEditContactForm(@PathVariable("hex-id") String contactHexId, Model model) throws ParseException {

        List<Contacts> contacts = contactService.showAllContacts(Util.keyContacts);
        Contacts contactToUpdate = contacts.stream()
                .filter(contact -> contact.getHexId().equals(contactHexId))
                .findFirst()
                .get();

        model.addAttribute("contact", contactToUpdate);

        return "editContact";
    }

    @PostMapping("/edit/{hex-id}")
    public String postEditContact(@PathVariable("hex-id") String contactHexId, @Valid @ModelAttribute("contact") Contacts contact,
            BindingResult result, Model model) throws ParseException {

        if (result.hasErrors()) {
            return "editContact";
        }

        List<Contacts> contacts = contactService.showAllContacts(Util.keyContacts);
        Contacts contactToUpdate = contacts.stream()
                .filter(c-> c.getHexId().equals(contactHexId))
                .findFirst()
                .get();

        Long contactToUpdateIndex = contactService.findIndex(Util.keyContacts, contactToUpdate);
        contactService.updateContact(Util.keyContacts, contactToUpdateIndex, contact);

        return "redirect:/contacts";
    }

    @GetMapping("/save")
    public String saveContactsToFile() throws ParseException, IOException {
        contactService.writeContactToFile(Util.keyContacts);
        return "saveContact";
    }

}
