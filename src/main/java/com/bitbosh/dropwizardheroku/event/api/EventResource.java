package com.bitbosh.dropwizardheroku.event.api;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.skife.jdbi.v2.DBI;

import com.bitbosh.dropwizardheroku.event.repository.EventDao;

import io.dropwizard.jersey.params.LongParam;

@Path("/events")
@Produces(MediaType.APPLICATION_JSON)
public class EventResource {

  private final EventDao eventDao;

  public EventResource(DBI jdbi) {
    eventDao = jdbi.onDemand(EventDao.class);
    eventDao.createEventDatabaseTable();
  }

  @GET
  public ApiResponse getEvents() {
    List<Event> eventList = eventDao.getEvents();
    
    // Sort events
    Collections.sort(eventList, new Comparator<Event>(){        
		@Override
		public int compare(Event event, Event otherEvent) {	
			int result = 0;
			if(otherEvent.getDate().before(event.getDate())){
				result = 1;
			} else if (event.getDate().before(otherEvent.getDate())){
				return -1;
			}
			return result;
		}
    });      
    
    ApiResponse response = new ApiResponse(eventList);
    return response;
  }

  @GET
  @Path("/{name}")
  public Event getEvent(@PathParam("name") String name) {
    Event event = eventDao.getEventByName(name);
    return event;
  }

  @POST
  public void createEvent(Event event) {
    eventDao.createEvent(event.getName(), event.getLocation(), event.getDescription(), event.getDate());
  }
  
  @DELETE
  @Path("/{id}")
  public void deleteEventById(@PathParam("id") LongParam id) {
    eventDao.deleteEventById(id.get());
  }
  
}
