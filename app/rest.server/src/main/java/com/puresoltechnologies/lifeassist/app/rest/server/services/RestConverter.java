package com.puresoltechnologies.lifeassist.app.rest.server.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.puresoltechnologies.lifeassist.app.api.calendar.Event;
import com.puresoltechnologies.lifeassist.app.api.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.api.calendar.Series;
import com.puresoltechnologies.lifeassist.app.api.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarEvent;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarSeries;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.EventType;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.Birthday;
import com.puresoltechnologies.lifeassist.app.rest.api.contacts.Contact;
import com.puresoltechnologies.lifeassist.app.rest.api.plugins.PluginDescription;
import com.puresoltechnologies.lifeassist.app.rest.api.services.ParameterDescription;

/**
 * Contains method which translate between REST model and application model.
 * 
 * @author Rick-Rainer Ludwig
 */
public class RestConverter {

    public static Reminder convert(com.puresoltechnologies.lifeassist.app.api.calendar.Reminder reminder) {
	return new Reminder(reminder.getAmount(), reminder.getUnit());
    }

    public static com.puresoltechnologies.lifeassist.app.api.calendar.Reminder convert(Reminder reminder) {
	return new com.puresoltechnologies.lifeassist.app.api.calendar.Reminder(reminder.getAmount(),
		reminder.getUnit());
    }

    public static CalendarEvent convert(Event entry) {
	Collection<com.puresoltechnologies.lifeassist.app.api.contacts.Contact> oldParticipants = entry
		.getParticipants();
	List<Contact> participants = new ArrayList<>();
	oldParticipants
		.forEach(e -> participants.add(new Contact(e.getId(), e.getName(), CalendarDay.of(e.getBirthday()))));
	ZonedDateTime begin = entry.getBegin();
	ZonedDateTime end = entry.getEnd();
	return new CalendarEvent(entry.getId(), entry.getType(), entry.getTitle(), entry.getDescription(), participants,
		entry.getReminder() != null, convert(entry.getReminder()), CalendarDay.of(begin.toLocalDate()),
		CalendarTime.of(begin.toLocalTime()), begin.getZone().getId(), CalendarDay.of(end.toLocalDate()),
		CalendarTime.of(end.toLocalTime()), end.getZone().getId(), entry.getOccupancy().name());
    }

    public static Event convert(CalendarEvent entry) {
	Collection<Contact> oldParticipants = entry.getParticipants();
	List<com.puresoltechnologies.lifeassist.app.api.contacts.Contact> participants = new ArrayList<>();
	oldParticipants.forEach(
		e -> participants.add(new com.puresoltechnologies.lifeassist.app.api.contacts.Contact(e.getId(),
			e.getName(), CalendarDay.toLocalDate(e.getBirthday()))));
	return new Event(entry.getId(), entry.getType(), entry.getTitle(), entry.getDescription(), participants,
		entry.isReminding() ? convert(entry.getReminder()) : null, entry.getBegin(), entry.getEnd(),
		OccupancyStatus.valueOf(entry.getOccupancy()));
    }

    public static Collection<CalendarEvent> convert(Collection<Event> entries) {
	List<CalendarEvent> convertedEntries = new ArrayList<>();
	entries.forEach(e -> convertedEntries.add(convert(e)));
	return convertedEntries;
    }

    public static Series convert(CalendarSeries series) {
	LocalDate startDate = CalendarDay.toLocalDate(series.getFirstOccurence());
	LocalTime time = CalendarTime.toLocalTime(series.getStartTime());
	ZoneId zonedId = ZoneId.of(series.getTimezone());
	ZonedDateTime firstOccurence = ZonedDateTime.of(startDate, time, zonedId);
	LocalDate lastOccurence = CalendarDay.toLocalDate(series.getLastOccurence());

	return new Series(series.getId(), series.getType(), series.getTitle(), series.getDescription(),
		convertPeople(series.getParticipants()), convert(series.getReminder()), firstOccurence, lastOccurence,
		series.getDurationAmount(), series.getDurationUnit(), OccupancyStatus.valueOf(series.getOccupancy()),
		Turnus.valueOf(series.getTurnus()), series.getSkipping());
    }

    public static CalendarSeries convert(Series series) {
	return new CalendarSeries(series.getId(), series.getType(), series.getTitle(), series.getDescription(),
		convertContactsToRest(series.getParticipants()), series.getReminder() != null,
		convert(series.getReminder()), CalendarDay.of(series.getFirstOccurence().toLocalDate()),
		CalendarDay.of(series.getLastOccurence()), series.getFirstOccurence().getZone().getId(),
		CalendarTime.of(series.getFirstOccurence().toLocalTime()), series.getDurationAmount(),
		series.getDurationUnit(), series.getOccupancy().name(), series.getTurnus().name(),
		series.getSkipping());
    }

    public static Collection<com.puresoltechnologies.lifeassist.app.api.contacts.Contact> convertPeople(
	    Collection<Contact> people) {
	List<com.puresoltechnologies.lifeassist.app.api.contacts.Contact> convertedPeople = new ArrayList<>();
	people.forEach(e -> convertedPeople.add(convert(e)));
	return convertedPeople;
    }

    public static Collection<Contact> convertContactsToRest(
	    Collection<com.puresoltechnologies.lifeassist.app.api.contacts.Contact> people) {
	List<Contact> convertedPeople = new ArrayList<>();
	people.forEach(e -> convertedPeople.add(convert(e)));
	return convertedPeople;
    }

    public static com.puresoltechnologies.lifeassist.app.api.contacts.Contact convert(Contact person) {
	return new com.puresoltechnologies.lifeassist.app.api.contacts.Contact(person.getId(), person.getName(),
		CalendarDay.toLocalDate(person.getBirthday()));
    }

    public static Contact convert(com.puresoltechnologies.lifeassist.app.api.contacts.Contact person) {
	return new Contact(person.getId(), person.getName(),
		person.getBirthday() != null ? CalendarDay.of(person.getBirthday()) : null);
    }

    public static List<EventType> convertEventTypes(
	    List<com.puresoltechnologies.lifeassist.app.api.calendar.EventType> entryTypes) {
	List<EventType> convertedEntryTypes = new ArrayList<>();
	entryTypes.forEach(e -> convertedEntryTypes.add(convert(e)));
	return convertedEntryTypes;
    }

    public static EventType convert(com.puresoltechnologies.lifeassist.app.api.calendar.EventType entryType) {
	return new EventType(entryType.getType(), entryType.getName());
    }

    public static List<TimeZoneInformation> convertTimezones(
	    List<com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation> timezones) {
	List<TimeZoneInformation> convertedTimezones = new ArrayList<>();
	timezones.forEach(e -> convertedTimezones.add(convert(e)));
	return convertedTimezones;
    }

    public static TimeZoneInformation convert(
	    com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation timezone) {
	return new TimeZoneInformation(timezone.getId(), timezone.getName(), timezone.getOffset());
    }

    public static List<DurationUnit> convertDurationUnits(
	    List<com.puresoltechnologies.lifeassist.app.api.calendar.DurationUnit> durationUnits) {
	List<DurationUnit> convertedDurationUnits = new ArrayList<>();
	durationUnits.forEach(e -> convertedDurationUnits.add(convert(e)));
	return convertedDurationUnits;
    }

    public static DurationUnit convert(com.puresoltechnologies.lifeassist.app.api.calendar.DurationUnit durationUnit) {
	return new DurationUnit(durationUnit.getUnit(), durationUnit.getName());
    }

    public static Collection<Birthday> convertBirthdays(
	    List<com.puresoltechnologies.lifeassist.app.api.contacts.Birthday> birthdays) {
	List<Birthday> convertedBirthdays = new ArrayList<>();
	birthdays.forEach(e -> convertedBirthdays.add(convert(e)));
	return convertedBirthdays;
    }

    private static Birthday convert(com.puresoltechnologies.lifeassist.app.api.contacts.Birthday birthday) {
	return new Birthday(birthday.getId(), birthday.getName(), CalendarDay.of(birthday.getBirthday()));
    }

    public static List<ParameterDescription> convertParameterDescriptions(
	    List<com.puresoltechnologies.lifeassist.app.api.settings.ParameterDescription> systemParmeters) {
	List<ParameterDescription> convertedSystemParmeters = new ArrayList<>();
	systemParmeters.forEach(e -> convertedSystemParmeters.add(convert(e)));
	return convertedSystemParmeters;
    }

    private static ParameterDescription convert(
	    com.puresoltechnologies.lifeassist.app.api.settings.ParameterDescription parameterDescription) {
	return new ParameterDescription(parameterDescription.getName(), parameterDescription.getType().name(),
		parameterDescription.getValue(), parameterDescription.getDefaultValue(), parameterDescription.getUnit(),
		parameterDescription.getDescription());
    }

    public static Set<PluginDescription> convertPluginDescriptions(
	    Set<com.puresoltechnologies.lifeassist.common.plugins.PluginDescription> pluginDescriptions) {
	Set<PluginDescription> convertedPluginDescriptions = new HashSet<>();
	pluginDescriptions.forEach(e -> convertedPluginDescriptions.add(convert(e)));
	return convertedPluginDescriptions;
    }

    private static PluginDescription convert(
	    com.puresoltechnologies.lifeassist.common.plugins.PluginDescription pluginDescription) {
	return new PluginDescription(pluginDescription.getName(), pluginDescription.getDescription());
    }

    /**
     * public constructor to avoid instantiation.
     */
    private RestConverter() {
    }

}
