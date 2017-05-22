package com.puresoltechnologies.lifeassist.app.rest.api.calendar;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

public interface CalendarService {

    public CalendarYear getYear(int year);

    public List<TimeZoneInformation> getTimezones(String dateTime, String language, String zoneId);

    public List<EntryType> getEntryTypes() throws SQLException;

    public List<DurationUnit> getDurationUnits();

    public List<DurationUnit> getReminderDurationUnits();

    public List<DurationUnit> getTurnusUnits();

    public Collection<CalendarEntry> getEntriesToday(String type) throws SQLException;

    public Collection<CalendarEntry> getEntriesTomorrow(String type) throws SQLException;

    public Collection<CalendarEntry> getEntries(String type, String from, String to) throws SQLException;

    public Response insertEntry(UriInfo uriInfo, CalendarEntry entry)
	    throws SQLException, IllegalArgumentException, IllegalAccessException;

    public boolean updateEntry(long id, CalendarEntry entry) throws SQLException;

    public CalendarEntry getEntry(long id) throws SQLException;

    public boolean removeEntry(long id) throws SQLException;

    public Response insertSeries(UriInfo uriInfo, CalendarSeries entrySerie)
	    throws SQLException, IllegalArgumentException, IllegalAccessException;

    public boolean updateSeries(long id, CalendarSeries entrySerie) throws SQLException;

    public CalendarSeries getEntrySerie(long id) throws SQLException;

    public boolean removeSeries(long id) throws SQLException;

    public Collection<CalendarEntry> getYearEntries(int year, String type) throws SQLException;

    public Collection<CalendarEntry> getMonthEntries(int year, int month, String type) throws SQLException;

    public Collection<CalendarEntry> getDayEntries(int year, int month, int day, String type) throws SQLException;

    public Collection<CalendarEntry> getDayEntries(int year, int week, String type) throws SQLException;

}
