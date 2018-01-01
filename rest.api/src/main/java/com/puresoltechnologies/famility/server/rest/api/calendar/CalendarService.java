package com.puresoltechnologies.famility.server.rest.api.calendar;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface CalendarService {

    public CalendarYear getYear(int year);

    public List<TimeZoneInformation> getTimezones(String dateTime, String language, String zoneId);

    public List<EventType> getEventTypes() throws SQLException;

    public List<DurationUnit> getDurationUnits();

    public List<DurationUnit> getReminderDurationUnits();

    public List<DurationUnit> getTurnusUnits();

    public Collection<CalendarEvent> getEntriesToday(String type) throws SQLException;

    public Collection<CalendarEvent> getEntriesTomorrow(String type) throws SQLException;

    public Collection<CalendarEvent> getEntries(String type, String from, String to) throws SQLException;

    public Response insertEvent(UriInfo uriInfo, CalendarEvent event)
	    throws SQLException, IllegalArgumentException, IllegalAccessException;

    public boolean updateEvent(long id, CalendarEvent event) throws SQLException;

    public CalendarEvent getEvent(long id) throws SQLException;

    public boolean removeEvent(long id) throws SQLException;

    public Response insertSeries(UriInfo uriInfo, CalendarSeries series)
	    throws SQLException, IllegalArgumentException, IllegalAccessException;

    public boolean updateSeries(long id, CalendarSeries series) throws SQLException;

    public CalendarSeries getSeries(long id) throws SQLException;

    public boolean removeSeries(long id) throws SQLException;

    public Collection<CalendarEvent> getYearEntries(int year, String type) throws SQLException;

    public Collection<CalendarEvent> getMonthEntries(int year, int month, String type) throws SQLException;

    public Collection<CalendarEvent> getDayEntries(int year, int month, int day, String type) throws SQLException;

    public Collection<CalendarEvent> getDayEntries(int year, int week, String type) throws SQLException;

}
