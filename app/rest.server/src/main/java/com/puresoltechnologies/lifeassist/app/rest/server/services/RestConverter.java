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

import com.puresoltechnologies.lifeassist.app.api.calendar.Entry;
import com.puresoltechnologies.lifeassist.app.api.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.api.calendar.Series;
import com.puresoltechnologies.lifeassist.app.api.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarEntry;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarSeries;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.rest.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.rest.api.people.Birthday;
import com.puresoltechnologies.lifeassist.app.rest.api.people.Person;
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

    public static CalendarEntry convert(Entry entry) {
	Collection<com.puresoltechnologies.lifeassist.app.api.people.Person> oldParticipants = entry.getParticipants();
	List<Person> participants = new ArrayList<>();
	oldParticipants
		.forEach(e -> participants.add(new Person(e.getId(), e.getName(), CalendarDay.of(e.getBirthday()))));
	ZonedDateTime begin = entry.getBegin();
	ZonedDateTime end = entry.getEnd();
	return new CalendarEntry(entry.getId(), entry.getType(), entry.getTitle(), entry.getDescription(), participants,
		entry.getReminder() != null, convert(entry.getReminder()), CalendarDay.of(begin.toLocalDate()),
		CalendarTime.of(begin.toLocalTime()), begin.getZone().getId(), CalendarDay.of(end.toLocalDate()),
		CalendarTime.of(end.toLocalTime()), end.getZone().getId(), entry.getOccupancy().name());
    }

    public static Entry convert(CalendarEntry entry) {
	Collection<Person> oldParticipants = entry.getParticipants();
	List<com.puresoltechnologies.lifeassist.app.api.people.Person> participants = new ArrayList<>();
	oldParticipants
		.forEach(e -> participants.add(new com.puresoltechnologies.lifeassist.app.api.people.Person(e.getId(),
			e.getName(), CalendarDay.toLocalDate(e.getBirthday()))));
	return new Entry(entry.getId(), entry.getType(), entry.getTitle(), entry.getDescription(), participants,
		entry.isReminding() ? convert(entry.getReminder()) : null, entry.getBegin(), entry.getEnd(),
		OccupancyStatus.valueOf(entry.getOccupancy()));
    }

    public static Collection<CalendarEntry> convert(Collection<Entry> entries) {
	List<CalendarEntry> convertedEntries = new ArrayList<>();
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
		convertPeopleToRest(series.getParticipants()), series.getReminder() != null,
		convert(series.getReminder()), CalendarDay.of(series.getFirstOccurence().toLocalDate()),
		CalendarDay.of(series.getLastOccurence()), series.getFirstOccurence().getZone().getId(),
		CalendarTime.of(series.getFirstOccurence().toLocalTime()), series.getDurationAmount(),
		series.getDurationUnit(), series.getOccupancy().name(), series.getTurnus().name(),
		series.getSkipping());
    }

    public static Collection<com.puresoltechnologies.lifeassist.app.api.people.Person> convertPeople(
	    Collection<Person> people) {
	List<com.puresoltechnologies.lifeassist.app.api.people.Person> convertedPeople = new ArrayList<>();
	people.forEach(e -> convertedPeople.add(convert(e)));
	return convertedPeople;
    }

    public static Collection<Person> convertPeopleToRest(
	    Collection<com.puresoltechnologies.lifeassist.app.api.people.Person> people) {
	List<Person> converterdPeople = new ArrayList<>();
	people.forEach(e -> converterdPeople.add(convert(e)));
	return converterdPeople;
    }

    public static com.puresoltechnologies.lifeassist.app.api.people.Person convert(Person person) {
	return new com.puresoltechnologies.lifeassist.app.api.people.Person(person.getName(),
		CalendarDay.toLocalDate(person.getBirthday()));
    }

    public static Person convert(com.puresoltechnologies.lifeassist.app.api.people.Person person) {
	return new Person(person.getName(), CalendarDay.of(person.getBirthday()));
    }

    public static List<EntryType> convertEntryTypes(
	    List<com.puresoltechnologies.lifeassist.app.api.calendar.EntryType> entryTypes) {
	List<EntryType> convertedEntryTypes = new ArrayList<>();
	entryTypes.forEach(e -> convertedEntryTypes.add(convert(e)));
	return convertedEntryTypes;
    }

    public static EntryType convert(com.puresoltechnologies.lifeassist.app.api.calendar.EntryType entryType) {
	return new EntryType(entryType.getType(), entryType.getName());
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
	    List<com.puresoltechnologies.lifeassist.app.api.people.Birthday> birthdays) {
	List<Birthday> convertedBirthdays = new ArrayList<>();
	birthdays.forEach(e -> convertedBirthdays.add(convert(e)));
	return convertedBirthdays;
    }

    private static Birthday convert(com.puresoltechnologies.lifeassist.app.api.people.Birthday birthday) {
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
