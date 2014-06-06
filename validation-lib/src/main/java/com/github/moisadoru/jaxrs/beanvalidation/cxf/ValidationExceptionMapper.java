package com.github.moisadoru.jaxrs.beanvalidation.cxf;

/**
 * Created by moisadoru on 6/6/14.
 */

import com.github.moisadoru.jaxrs.beanvalidation.representation.ValidationFailure;
import org.apache.cxf.validation.ResponseConstraintViolationException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        if (!(exception instanceof ConstraintViolationException) || (exception instanceof ResponseConstraintViolationException)) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        final ConstraintViolationException constraint = (ConstraintViolationException) exception;
        ValidationFailure vf = new ValidationFailure();

        vf.setType("VALIDATION_FAILED");
        vf.setMessage("The input data is invalid");
        vf.setMoreInfo("https://example.com/documentation/validation");

        for (final ConstraintViolation<?> violation : constraint.getConstraintViolations()) {
            vf.add(violation);
        }

        return Response.status(Response.Status.BAD_REQUEST).entity(vf).build();
    }
}
