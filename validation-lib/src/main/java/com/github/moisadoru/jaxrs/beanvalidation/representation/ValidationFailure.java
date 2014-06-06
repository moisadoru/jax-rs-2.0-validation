package com.github.moisadoru.jaxrs.beanvalidation.representation;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tmoisa on 6/6/14.
 */
@XmlRootElement(name = "error")
@XmlType(propOrder = {"type", "message", "moreInfo", "errors"})
public class ValidationFailure implements Serializable {

    private static final long serialVersionUID = 1625992821977268269L;

    private String type;

    private String message;

    @XmlTransient
    private String moreInfo;

    @XmlTransient
    List<ValidationError> errors;

    public ValidationFailure() {
    }

    public String getType() {
        return type;
    }

    public void add(ValidationError validationError) {
        if (validationError != null) {
            getErrors().add(validationError);
        }
    }

    public void add(String type, String message, String field) {
        ValidationError validationError = new ValidationError();
        validationError.setMessage(message);
        validationError.setType(type);
        validationError.setField(field);
        add(validationError);
    }
    
    public void add(final ConstraintViolation<?> violation) {
        String type = violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName().toUpperCase();
        add(type, violation.getMessage(), nodeName(violation.getPropertyPath()));
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @XmlElement(name="more_info")
    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    @XmlElementWrapper(name = "errors")
    @XmlElement (name="error", type = ValidationError.class)
    public List<ValidationError> getErrors() {
        if (errors == null) {
            errors = new ArrayList<>();
        }
        return errors;
    }

    private String nodeName(Path path) {
        String crt = null;
        for (Path.Node node : path) {
            crt = node.getName();
        }
        return crt;
    }
}
