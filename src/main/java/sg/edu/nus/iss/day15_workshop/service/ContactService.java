package sg.edu.nus.iss.day15_workshop.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.day15_workshop.model.Contacts;
import sg.edu.nus.iss.day15_workshop.repository.ListRepo;

@Service
public class ContactService {

    private final ListRepo contactRepo;
    private List<Contacts> contactsList;

    @Value("${data.dir}")
    private final String dataDir;

    public ContactService(ListRepo contactRepo, String dataDir) {
        this.contactRepo = contactRepo;
        this.dataDir = dataDir;
        contactsList = new ArrayList<>();
    }

    public void addContact(String redisKey, Contacts contact) {
        String contactToAdd = contact.toString().toLowerCase();
        contactRepo.rightPush(redisKey, contactToAdd);
    }

    public Long findIndex(String redisKey, Contacts contactBeforeUpdate) {

        Long indexToUpdateContact = contactRepo.findIndex(redisKey, contactBeforeUpdate);
        return indexToUpdateContact;

    }

    public void updateContact(String redisKey, Long indexToUpdateContact, Contacts updatedContact) {
        // Long indexToUpdateContact = findIndex(redisKey, updatedContact);
        contactRepo.updateContact(redisKey, indexToUpdateContact, updatedContact);
    }

    public List<Contacts> showAllContacts(String redisKey) throws ParseException {

        // fetch the raw list of data from the repository
        List<Object> listData = contactRepo.getList(redisKey);
        System.out.println("Retreived data from Redis: " + listData);
        List<Contacts> tempContactsList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    

        for (Object contact : listData) {
            try {
                String[] rawData = contact.toString().split(",");
                if (rawData.length < 7) {
                    System.err.println("Invalid data format: " + contact);
                    continue; // Skip invalid entries
                }
                String id = rawData[0].trim();
                System.out.println(id);
                String hexId = rawData[1].trim();
                System.out.println(hexId);
                String name = rawData[2].trim();
                System.out.println(name);
                String email = rawData[3].trim();
                System.out.println(email);
                String phoneNumber = rawData[4].trim();
                System.out.println(phoneNumber);
                LocalDate dateOfBirth = LocalDate.parse(rawData[5].trim(), formatter);
                System.out.println(dateOfBirth);
                Integer postalCode = Integer.parseInt(rawData[6].trim());
                System.out.println(postalCode);

                // Create a contact object and add it to the list
                Contacts c = new Contacts(id, hexId, name, email, phoneNumber, dateOfBirth, postalCode);
                tempContactsList.add(c);
               
            } catch (NumberFormatException e) {
                System.err.println("Error parsing contact data: " + contact + " - " + e.getMessage());
            }
            
        }
        contactsList = tempContactsList;

        return contactsList;
    }

    public Boolean deleteContact(String redisKey, Contacts contactToDelete) {
        return contactRepo.deleteContact(redisKey, contactToDelete);
    }

    // write to file
    public void writeContactToFile(String redisKey) throws ParseException, IOException {
        // i need my list of contacts
        // i need my path object
        // file name -> hexId field of Contacts

        // Fetch list of Contacts
        List<Contacts> contacts = showAllContacts(redisKey);

        // Generate file paths for contacts
        List<Path> contactFilePaths = contactFilePath(contacts);

        for (Path contactFilePath : contactFilePaths) {

            // Find the corresponding contact for the file path
            Contacts contact = contacts.stream()
                    .filter(c -> c.getHexId().equalsIgnoreCase(contactFilePath.getFileName().toString()))
                    .findFirst()
                    .get();

            // Ensure that file exists, create if it does not
            if (!Files.exists(contactFilePath)) {
                Files.createFile(contactFilePath);
            }

            // Write contact details to the file
            try (BufferedWriter writer = Files.newBufferedWriter(contactFilePath, StandardCharsets.UTF_8)) {
                writer.write(contact.getId() + "\n");
                writer.write(contact.getHexId() + "\n");
                writer.write(contact.getName() + "\n");
                writer.write(contact.getEmail() + "\n");
                writer.write(contact.getDateOfBirth() + "\n");
                writer.write(contact.getPostalCode() + "\n");

            } catch (IOException e) {
                System.err.println("Error writing to file: " + contactFilePath);
            }
        }

    }

    public List<Path> contactFilePath(List<Contacts> contactsList) {

        List<Path> filePathList = new ArrayList<>();

        for (Contacts contact : contactsList) {
            String filename = contact.getHexId();
            Path filePath = Paths.get(dataDir, filename);
            filePathList.add(filePath);
        }

        return filePathList;

    }

}
