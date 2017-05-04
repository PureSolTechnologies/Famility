package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.mysema.commons.lang.CloseableIterator;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.calendar.EntryType;
import com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.QEntries;
import com.puresoltechnologies.lifeassist.app.model.QEntrySeries;
import com.puresoltechnologies.lifeassist.app.model.QEntryTypes;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLUpdateClause;

public class CalendarManager {

    private static final Field entryIdField;
    private static final Field entrySerieIdField;
    static {
	try {
	    entryIdField = Entry.class.getDeclaredField("id");
	    entryIdField.setAccessible(true);
	    entrySerieIdField = EntrySerie.class.getDeclaredField("id");
	    entrySerieIdField.setAccessible(true);
	} catch (NoSuchFieldException | SecurityException e) {
	    throw new RuntimeException(e);
	}
    }

    public List<DurationUnit> getDurationUnits() {
	List<DurationUnit> timeUnits = new ArrayList<>();
	timeUnits.add(new DurationUnit(ChronoUnit.MINUTES, "Minutes"));
	timeUnits.add(new DurationUnit(ChronoUnit.HOURS, "Hours"));
	timeUnits.add(new DurationUnit(ChronoUnit.DAYS, "Days"));
	return timeUnits;
    }

    public List<DurationUnit> getReminderDurationUnits() {
	List<DurationUnit> timeUnits = new ArrayList<>();
	timeUnits.add(new DurationUnit(ChronoUnit.MINUTES, "Minutes"));
	timeUnits.add(new DurationUnit(ChronoUnit.HOURS, "Hours"));
	timeUnits.add(new DurationUnit(ChronoUnit.DAYS, "Days"));
	return timeUnits;
    }

    public List<DurationUnit> getTurnusUnits() {
	List<DurationUnit> timeUnits = new ArrayList<>();
	timeUnits.add(new DurationUnit(ChronoUnit.WEEKS, "Weekly"));
	timeUnits.add(new DurationUnit(ChronoUnit.MONTHS, "Monthly"));
	timeUnits.add(new DurationUnit(ChronoUnit.YEARS, "Yearly"));
	return timeUnits;
    }

    public List<EntryType> getEntryTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory.select(QEntryTypes.entryTypes.type, QEntryTypes.entryTypes.name)
		    .from(QEntryTypes.entryTypes);
	    List<EntryType> entryTypes = new ArrayList<>();
	    for (Tuple tuple : query.fetch()) {
		String type = tuple.get(QEntryTypes.entryTypes.type);
		String name = tuple.get(QEntryTypes.entryTypes.name);
		entryTypes.add(new EntryType(type, name));
	    }
	    return entryTypes;
	}
    }

    public List<TimeZoneInformation> getTimezones(Instant instant, Locale locale) {
	List<TimeZoneInformation> timezones = new ArrayList<>();
	for (String zoneId : ZoneId.getAvailableZoneIds()) {
	    // if (zoneId.startsWith("Africa") || zoneId.startsWith("America")
	    // || zoneId.startsWith("Antarctica")
	    // || zoneId.startsWith("Arctic") || zoneId.startsWith("Asia") ||
	    // zoneId.startsWith("Atlantic")
	    // || zoneId.startsWith("Australia") || zoneId.startsWith("Europe")
	    // || zoneId.startsWith("Pacific")) {
	    if (zoneId.contains("/")) {
		ZoneId zoneInfo = ZoneId.of(zoneId);
		String displayName = zoneInfo.getDisplayName(TextStyle.FULL, locale);
		timezones.add(new TimeZoneInformation(zoneId, displayName,
			zoneInfo.getRules().getOffset(instant).getTotalSeconds() / 3600));
	    }
	}
	Collections.sort(timezones);
	return timezones;
    }

    public long insertEntry(Entry entry) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(entry.getDate());
	    LocalTime time = CalendarTime.toLocalTime(entry.getTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, entry.getZoneId());
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("entry_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QEntries.entries)//
		    .set(QEntries.entries.id, id) //
		    .set(QEntries.entries.type, entry.getType()) //
		    .set(QEntries.entries.title, entry.getTitle()) //
		    .set(QEntries.entries.description, entry.getDescription()) //
		    .set(QEntries.entries.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QEntries.entries.timezone, entry.getTimezone()) //
		    .set(QEntries.entries.durationAmount, entry.getDurationAmount()) //
		    .set(QEntries.entries.durationUnit, entry.getDurationUnit().name()) //
		    .set(QEntries.entries.reminderAmount, entry.getReminder().getAmount()) //
		    .set(QEntries.entries.reminderUnit, entry.getReminder().getUnit().name()) //
		    .set(QEntries.entries.occupancy, entry.getOccupancy().name()) //
		    .execute();
	    queryFactory.commit();
	    entryIdField.set(entry, id);
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateEntry(Entry entry) throws SQLException {
	return updateEntry(entry.getId(), entry);
    }

    public boolean updateEntry(long id, Entry entry) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(entry.getDate());
	    LocalTime time = CalendarTime.toLocalTime(entry.getTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, entry.getZoneId());
	    long updated = queryFactory//
		    .update(QEntries.entries)//
		    .set(QEntries.entries.type, entry.getType()) //
		    .set(QEntries.entries.title, entry.getTitle()) //
		    .set(QEntries.entries.description, entry.getDescription()) //
		    .set(QEntries.entries.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QEntries.entries.timezone, entry.getTimezone()) //
		    .set(QEntries.entries.durationAmount, entry.getDurationAmount()) //
		    .set(QEntries.entries.durationUnit, entry.getDurationUnit().name()) //
		    .set(QEntries.entries.reminderAmount, entry.getReminder().getAmount()) //
		    .set(QEntries.entries.reminderUnit, entry.getReminder().getUnit().name()) //
		    .set(QEntries.entries.occupancy, entry.getOccupancy().name()) //
		    .where(QEntries.entries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public Entry getEntry(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QEntries.entries.all()) //
		    .from(QEntries.entries) //
		    .where(QEntries.entries.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToEntry(tuple);
	}
    }

    private Entry convertTupleToEntry(Tuple tuple) {
	String type = tuple.get(QEntries.entries.type);
	String title = tuple.get(QEntries.entries.title);
	String description = tuple.get(QEntries.entries.description);
	LocalDateTime occurrence = tuple.get(QEntries.entries.occurrence).toLocalDateTime();
	String timezone = tuple.get(QEntries.entries.timezone);

	int durationAmount = tuple.get(QEntries.entries.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QEntries.entries.durationUnit));

	int reminderAmount = tuple.get(QEntries.entries.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QEntries.entries.reminderUnit));

	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QEntries.entries.occupancy));
	return new Entry(type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(occurrence), timezone,
		CalendarTime.of(occurrence), durationAmount, durationUnit, occupancy);
    }

    public boolean removeEntry(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QEntries.entries) //
		    .where(QEntries.entries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public long insertEntrySerie(EntrySerie entrySerie) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(entrySerie.getStartDate());
	    LocalTime time = CalendarTime.toLocalTime(entrySerie.getStartTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, entrySerie.getZoneId());
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("entry_serie_id_seq	"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QEntrySeries.entrySeries)//
		    .set(QEntrySeries.entrySeries.id, id) //
		    .set(QEntrySeries.entrySeries.type, entrySerie.getType()) //
		    .set(QEntrySeries.entrySeries.title, entrySerie.getTitle()) //
		    .set(QEntrySeries.entrySeries.description, entrySerie.getDescription()) //
		    .set(QEntrySeries.entrySeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QEntrySeries.entrySeries.timezone, entrySerie.getTimezone()) //
		    .set(QEntrySeries.entrySeries.durationAmount, entrySerie.getDurationAmount()) //
		    .set(QEntrySeries.entrySeries.durationUnit, entrySerie.getDurationUnit().name()) //
		    .set(QEntrySeries.entrySeries.reminderAmount, entrySerie.getReminder().getAmount()) //
		    .set(QEntrySeries.entrySeries.reminderUnit, entrySerie.getReminder().getUnit().name()) //
		    .set(QEntrySeries.entrySeries.occupancy, entrySerie.getOccupancy().name()) //
		    .set(QEntrySeries.entrySeries.turnus, entrySerie.getTurnus().name()) //
		    .set(QEntrySeries.entrySeries.skipping, entrySerie.getSkipping()) //
		    .execute();
	    queryFactory.commit();
	    entrySerieIdField.set(entrySerie, id);
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateEntrySerie(EntrySerie entrySerie) throws SQLException {
	return updateEntrySerie(entrySerie.getId(), entrySerie);
    }

    public boolean updateEntrySerie(long id, EntrySerie entrySerie) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(entrySerie.getStartDate());
	    LocalTime time = CalendarTime.toLocalTime(entrySerie.getStartTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, entrySerie.getZoneId());
	    long updated = queryFactory//
		    .update(QEntrySeries.entrySeries)//
		    .set(QEntrySeries.entrySeries.type, entrySerie.getType()) //
		    .set(QEntrySeries.entrySeries.title, entrySerie.getTitle()) //
		    .set(QEntrySeries.entrySeries.description, entrySerie.getDescription()) //
		    .set(QEntrySeries.entrySeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QEntrySeries.entrySeries.timezone, entrySerie.getTimezone()) //
		    .set(QEntrySeries.entrySeries.durationAmount, entrySerie.getDurationAmount()) //
		    .set(QEntrySeries.entrySeries.durationUnit, entrySerie.getDurationUnit().name()) //
		    .set(QEntrySeries.entrySeries.reminderAmount, entrySerie.getReminder().getAmount()) //
		    .set(QEntrySeries.entrySeries.reminderUnit, entrySerie.getReminder().getUnit().name()) //
		    .set(QEntrySeries.entrySeries.occupancy, entrySerie.getOccupancy().name()) //
		    .set(QEntrySeries.entrySeries.turnus, entrySerie.getTurnus().name()) //
		    .set(QEntrySeries.entrySeries.skipping, entrySerie.getSkipping()) //
		    .where(QEntrySeries.entrySeries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public EntrySerie getEntrySerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QEntrySeries.entrySeries.all()) //
		    .from(QEntrySeries.entrySeries) //
		    .where(QEntrySeries.entrySeries.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToEntrySerie(tuple);
	}
    }

    private EntrySerie convertTupleToEntrySerie(Tuple tuple) {
	String type = tuple.get(QEntrySeries.entrySeries.type);
	String title = tuple.get(QEntrySeries.entrySeries.title);
	String description = tuple.get(QEntrySeries.entrySeries.description);
	LocalDateTime firstOccurrence = tuple.get(QEntrySeries.entrySeries.firstOccurrence).toLocalDateTime();
	String timezone = tuple.get(QEntrySeries.entrySeries.timezone);

	int durationAmount = tuple.get(QEntrySeries.entrySeries.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QEntries.entries.durationUnit));

	int reminderAmount = tuple.get(QEntrySeries.entrySeries.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QEntrySeries.entrySeries.reminderUnit));
	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QEntrySeries.entrySeries.occupancy));
	Turnus turnus = Turnus.valueOf(Turnus.class, tuple.get(QEntrySeries.entrySeries.turnus));
	int skipping = tuple.get(QEntrySeries.entrySeries.skipping);
	return new EntrySerie(type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(firstOccurrence), timezone,
		CalendarTime.of(firstOccurrence), durationAmount, durationUnit, occupancy, turnus, skipping);
    }

    public boolean removeEntrySerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QEntrySeries.entrySeries) //
		    .where(QEntrySeries.entrySeries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public Collection<Entry> getEntriesToday(String type) throws SQLException {
	LocalDate today = LocalDate.now();
	return getDayEntries(today.getYear(), today.getMonth().getValue(), today.getDayOfMonth(), type);
    }

    public Collection<Entry> getEntriesTomorrow(String type) throws SQLException {
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = today.plusDays(1);
	return getDayEntries(tomorrow.getYear(), tomorrow.getMonth().getValue(), tomorrow.getDayOfMonth(), type);
    }

    public Collection<Entry> getYearEntries(int year, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, 1, 1, 0, 0);
	LocalDateTime to = LocalDateTime.of(year, 12, 31, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Entry> getMonthEntries(int year, int month, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0);
	int lastDay = Month.of(month).length(Year.of(year).isLeap());
	LocalDateTime to = LocalDateTime.of(year, month, lastDay, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Entry> getDayEntries(int year, int month, int day, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, month, day, 0, 0);
	LocalDateTime to = LocalDateTime.of(year, month, day, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Entry> getEntriesBetween(String type, LocalDateTime from, LocalDateTime to) throws SQLException {
	checkAndCreateSerieEntries(type, to.toLocalDate());
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QEntries.entries.all()).from(QEntries.entries) //
		    .where(QEntries.entries.occurrence.between(Timestamp.valueOf(from), Timestamp.valueOf(to)));
	    if (type != null) {
		select.where(QEntries.entries.type.eq(type));
	    }
	    List<Entry> entries = new ArrayList<>();
	    for (Tuple tuple : select.fetch()) {
		entries.add(convertTupleToEntry(tuple));
	    }
	    return entries;
	}
    }

    private void checkAndCreateSerieEntries(String type, LocalDate to) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> seriesNeedingUpdate = queryFactory.select(QEntrySeries.entrySeries.all())
		    .where(QEntrySeries.entrySeries.entriesCreatedTo.before(Date.valueOf(to)));
	    if (type != null) {
		seriesNeedingUpdate.where(QEntrySeries.entrySeries.type.eq(type));
	    }
	    try (CloseableIterator<Tuple> iterator = seriesNeedingUpdate.iterate()) {
		while (iterator.hasNext()) {
		    Tuple tuple = iterator.next();
		    EntrySerie entrySerie = convertTupleToEntrySerie(tuple);
		    Date lastDate = tuple.get(QEntrySeries.entrySeries.entriesCreatedTo);
		    createSerieEntries(queryFactory, entrySerie, lastDate.toLocalDate(), to);
		}
	    }
	}
    }

    private void createSerieEntries(SQLQueryFactory queryFactory, EntrySerie entrySerie, LocalDate from, LocalDate to)
	    throws SQLException {
	Turnus turnus = entrySerie.getTurnus();
	LocalDate current = from.plus(turnus.getAmout(), turnus.getUnit());
	while (current.isBefore(to) || current.isEqual(to)) {
	    CalendarDay date = CalendarDay.of(current);
	    Entry entry = new Entry(entrySerie.getType(), entrySerie.getTitle(), entrySerie.getDescription(),
		    entrySerie.getParticipants(), entrySerie.getReminder() != null ? true : false,
		    entrySerie.getReminder(), date, entrySerie.getTimezone(), entrySerie.getStartTime(),
		    entrySerie.getDurationAmount(), entrySerie.getDurationUnit(), entrySerie.getOccupancy());
	    insertEntry(entry);
	    current = current.plus(turnus.getAmout(), turnus.getUnit());
	}
	SQLUpdateClause update = queryFactory.update(QEntrySeries.entrySeries).set(QEntrySeries.entrySeries.entriesCreatedTo, Date.valueOf(to))
		.where(QEntrySeries.entrySeries.id.eq(entrySerie.getId()));
	update.execute();
    }
}
