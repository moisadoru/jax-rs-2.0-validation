package com.github.moisadoru.jaxrs.beanvalidation.example.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;

@XmlRootElement(name="person")
//@XMLName("http://{person}person")
public class Person {
    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 2, max = 128)
    private String firstName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{2,}$")
    private String lastName;
        
    public Person() {
    }
    
    public Person( final String email ) {
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail( final String email ) {
        this.email = email;
    }
        
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setFirstName( final String firstName ) {
        this.firstName = firstName;
    }
    
    public void setLastName( final String lastName ) {
        this.lastName = lastName;
    }        
}
