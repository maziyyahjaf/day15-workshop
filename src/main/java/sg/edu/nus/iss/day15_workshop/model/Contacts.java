package sg.edu.nus.iss.day15_workshop.model;

import java.time.LocalDate;
import java.util.UUID;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class Contacts {
    
    private String id;

    private String hexId;

    @NotEmpty
    @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters")
    private String name;

    @NotEmpty
    @Email(message = "Please use proper email formatting")
    private String email;

    @NotEmpty
    @Pattern(regexp = "(8|9)[0-9]{7}", message = "Mobile number must start with 8 or 9, followed by 7 digits")
    private String phoneNumber;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Digits(fraction = 0, integer = 6, message = "Postal Code must be 6 digits")
    @Min(value = 111111, message = "Postal Code starts at 111111") 
    @Max(value = 999999, message = "Postal Code cannot exceed 999999")
    private Integer postalCode;

    

    public Contacts() {
    }

    public Contacts(

            @NotEmpty @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters") String name,
            @Email(message = "Please use proper email formatting") String email,
            @Pattern(regexp = "(8|9)[0-9]{7}", message = "Mobile number must start with 8 or 9, followed by 7 digits") String phoneNumber,
            @Past LocalDate dateOfBirth,
            @Digits(fraction = 0, integer = 6, message = "Postal Code must be 6 digits")
            @Min(value = 111111, message = "Postal Code starts at 111111") 
            @Max(value = 999999, message = "Postal Code cannot exceed 999999") 
            Integer postalCode) {


        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
        this.id = UUID.randomUUID().toString();
        this.hexId = randomDataGenerator.nextHexString(8);
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
    }

    
    public Contacts(String id, String hexId,
            @NotEmpty @Size(min = 3, max = 64, message = "Name must be between 3 and 64 characters") String name,
            @NotEmpty @Email(message = "Please use proper email formatting") String email,
            @NotEmpty @Pattern(regexp = "(8|9)[0-9]{7}", message = "Mobile number must start with 8 or 9, followed by 7 digits") String phoneNumber,
            @Past @NotEmpty LocalDate dateOfBirth,
            @Digits(fraction = 0, integer = 6, message = "Postal Code must be 6 digits") @Min(value = 111111, message = "Postal Code starts at 111111") @Max(value = 999999, message = "Postal Code cannot exceed 999999") @NotEmpty Integer postalCode) {
        this.id = id;
        this.hexId = hexId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.postalCode = postalCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getHexId() {
        return hexId;
    }

    public void setHexId(String hexId) {
        this.hexId = hexId;
    }

    @Override
    public String toString() {
        return id + "," + hexId + "," + name + "," + email + "," + phoneNumber
                + "," + dateOfBirth + "," + postalCode;
    }

    

    


}
