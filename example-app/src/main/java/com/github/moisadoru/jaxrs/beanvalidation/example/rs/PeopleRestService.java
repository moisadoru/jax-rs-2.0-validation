package com.github.moisadoru.jaxrs.beanvalidation.example.rs;

import java.util.Collection;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.cxf.annotations.SchemaValidation;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

import com.github.moisadoru.jaxrs.beanvalidation.example.model.Person;
import com.github.moisadoru.jaxrs.beanvalidation.example.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/people")
@SchemaValidation
public class PeopleRestService {
    @Autowired
    private PeopleService peopleService;
    
    @Produces( { MediaType.APPLICATION_JSON } )
    @GET
    public @Valid Collection< Person > getPeople(
    		@Min( 1 ) @QueryParam( "count" ) @DefaultValue( "1" ) final int count ) {
        return peopleService.getPeople( count );
    }

    @Produces( { MediaType.APPLICATION_JSON } )
    @Path( "/{email}" )
    @GET
    public @Valid Person getPerson( @NotNull @Length( min = 5, max = 255 ) @PathParam( "email" ) final String email ) {
        return peopleService.getByEmail( email );
    }

    @Valid
    @Produces( { MediaType.APPLICATION_JSON  } )
    @POST
    public Response addPerson( @Context final UriInfo uriInfo,
                               @NotNull @Email @Length(min=5, max=255) @FormParam( "email" ) final String email,
                               @FormParam( "firstName" ) final String firstName,
                               @FormParam( "lastName" ) final String lastName )
    {
        final Person person = peopleService.addPerson( email, firstName, lastName );
        return Response.created( uriInfo.getRequestUriBuilder().path( email ).build() ).entity( person ).build();
    }

    @POST
    @Valid
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Person addPerson(@Valid Person person)
    {
        return peopleService.addPerson(person);
    }

    @DELETE
    public Response deleteAll() {
		peopleService.clear();
		return Response.ok().build();
	}
}
