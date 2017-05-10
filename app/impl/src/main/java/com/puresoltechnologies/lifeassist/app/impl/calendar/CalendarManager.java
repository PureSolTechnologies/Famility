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
import com.puresoltechnologies.lifeassist.app.model.QCalendarEntries;
import com.puresoltechnologies.lifeassist.app.model.QCalendarEntryTypes;
import com.puresoltechnologies.lifeassist.app.model.QCalendarSeries;
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
	    SQLQuery<Tuple> query = queryFactory
		    .select(QCalendarEntryTypes.calendarEntryTypes.type, QCalendarEntryTypes.calendarEntryTypes.name)
		    .from(QCalendarEntryTypes.calendarEntryTypes);
	    List<EntryType> entryTypes = new ArrayList<>();
	    for (Tuple tuple : query.fetch()) {
		String type = tuple.get(QCalendarEntryTypes.calendarEntryTypes.type);
		String name = tuple.get(QCalendarEntryTypes.calendarEntryTypes.name);
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
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("calendar_entry_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QCalendarEntries.calendarEntries) //
		    .set(QCalendarEntries.calendarEntries.id, id) //
		    .set(QCalendarEntries.calendarEntries.type, entry.getType()) //
		    .set(QCalendarEntries.calendarEntries.title, entry.getTitle()) //
		    .set(QCalendarEntries.calendarEntries.description, entry.getDescription()) //
		    .set(QCalendarEntries.calendarEntries.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QCalendarEntries.calendarEntries.timezone, entry.getTimezone()) //
		    .set(QCalendarEntries.calendarEntries.durationAmount, entry.getDurationAmount()) //
		    .set(QCalendarEntries.calendarEntries.durationUnit, entry.getDurationUnit().name()) //
		    .set(QCalendarEntries.calendarEntries.reminderAmount, entry.getReminder().getAmount()) //
		    .set(QCalendarEntries.calendarEntries.reminderUnit, entry.getReminder().getUnit().name()) //
		    .set(QCalendarEntries.calendarEntries.occupancy, entry.getOccupancy().name()) //
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
		    .update(QCalendarEntries.calendarEntries)//
		    .set(QCalendarEntries.calendarEntries.type, entry.getType()) //
		    .set(QCalendarEntries.calendarEntries.title, entry.getTitle()) //
		    .set(QCalendarEntries.calendarEntries.description, entry.getDescription()) //
		    .set(QCalendarEntries.calendarEntries.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QCalendarEntries.calendarEntries.timezone, entry.getTimezone()) //
		    .set(QCalendarEntries.calendarEntries.durationAmount, entry.getDurationAmount()) //
		    .set(QCalendarEntries.calendarEntries.durationUnit, entry.getDurationUnit().name()) //
		    .set(QCalendarEntries.calendarEntries.reminderAmount, entry.getReminder().getAmount()) //
		    .set(QCalendarEntries.calendarEntries.reminderUnit, entry.getReminder().getUnit().name()) //
		    .set(QCalendarEntries.calendarEntries.occupancy, entry.getOccupancy().name()) //
		    .where(QCalendarEntries.calendarEntries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public Entry getEntry(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QCalendarEntries.calendarEntries.all()) //
		    .from(QCalendarEntries.calendarEntries) //
		    .where(QCalendarEntries.calendarEntries.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToEntry(tuple);
	}
    }

    private Entry convertTupleToEntry(Tuple tuple) {
	long id = tuple.get(QCalendarEntries.calendarEntries.id);
	String type = tuple.get(QCalendarEntries.calendarEntries.type);
	String title = tuple.get(QCalendarEntries.calendarEntries.title);
	String description = tuple.get(QCalendarEntries.calendarEntries.description);
	LocalDateTime occurrence = tuple.get(QCalendarEntries.calendarEntries.occurrence).toLocalDateTime();
	String timezone = tuple.get(QCalendarEntries.calendarEntries.timezone);

	int durationAmount = tuple.get(QCalendarEntries.calendarEntries.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QCalendarEntries.calendarEntries.durationUnit));

	int reminderAmount = tuple.get(QCalendarEntries.calendarEntries.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QCalendarEntries.calendarEntries.reminderUnit));

	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QCalendarEntries.calendarEntries.occupancy));
	return new Entry(id, type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(occurrence), timezone,
		CalendarTime.of(occurrence), durationAmount, durationUnit, occupancy);
    }

    public boolean removeEntry(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QCalendarEntries.calendarEntries) //
		    .where(QCalendarEntries.calendarEntries.id.eq(id)) //
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
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("calendar_series_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QCalendarSeries.calendarSeries)//
		    .set(QCalendarSeries.calendarSeries.id, id) //
		    .set(QCalendarSeries.calendarSeries.type, entrySerie.getType()) //
		    .set(QCalendarSeries.calendarSeries.title, entrySerie.getTitle()) //
		    .set(QCalendarSeries.calendarSeries.description, entrySerie.getDescription()) //
		    .set(QCalendarSeries.calendarSeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QCalendarSeries.calendarSeries.lastOccurrence,
			    entrySerie.getLastDate() != null ? Date.valueOf(entrySerie.getLastDate().getLocalDate())
				    : null) //
		    .set(QCalendarSeries.calendarSeries.timezone, entrySerie.getTimezone()) //
		    .set(QCalendarSeries.calendarSeries.durationAmount, entrySerie.getDurationAmount()) //
		    .set(QCalendarSeries.calendarSeries.durationUnit, entrySerie.getDurationUnit().name()) //
		    .set(QCalendarSeries.calendarSeries.reminderAmount, entrySerie.getReminder().getAmount()) //
		    .set(QCalendarSeries.calendarSeries.reminderUnit, entrySerie.getReminder().getUnit().name()) //
		    .set(QCalendarSeries.calendarSeries.occupancy, entrySerie.getOccupancy().name()) //
		    .set(QCalendarSeries.calendarSeries.turnus, entrySerie.getTurnus().name()) //
		    .set(QCalendarSeries.calendarSeries.skipping, entrySerie.getSkipping()) //
		    .set(QCalendarSeries.calendarSeries.lastEntryCreated,
			    Date.valueOf(CalendarDay.toLocalDate(entrySerie.getStartDate()).minus(1, ChronoUnit.DAYS))) //
		    .execute();

	    Entry entry = new Entry(entrySerie.getType(), entrySerie.getTitle(), entrySerie.getDescription(),
		    entrySerie.getParticipants(), entrySerie.getReminder() != null ? true : false,
		    entrySerie.getReminder(), entrySerie.getStartDate(), entrySerie.getTimezone(),
		    entrySerie.getStartTime(), entrySerie.getDurationAmount(), entrySerie.getDurationUnit(),
		    entrySerie.getOccupancy());
	    insertEntry(entry);

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
		    .update(QCalendarSeries.calendarSeries)//
		    .set(QCalendarSeries.calendarSeries.type, entrySerie.getType()) //
		    .set(QCalendarSeries.calendarSeries.title, entrySerie.getTitle()) //
		    .set(QCalendarSeries.calendarSeries.description, entrySerie.getDescription()) //
		    .set(QCalendarSeries.calendarSeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QCalendarSeries.calendarSeries.timezone, entrySerie.getTimezone()) //
		    .set(QCalendarSeries.calendarSeries.durationAmount, entrySerie.getDurationAmount()) //
		    .set(QCalendarSeries.calendarSeries.durationUnit, entrySerie.getDurationUnit().name()) //
		    .set(QCalendarSeries.calendarSeries.reminderAmount, entrySerie.getReminder().getAmount()) //
		    .set(QCalendarSeries.calendarSeries.reminderUnit, entrySerie.getReminder().getUnit().name()) //
		    .set(QCalendarSeries.calendarSeries.occupancy, entrySerie.getOccupancy().name()) //
		    .set(QCalendarSeries.calendarSeries.turnus, entrySerie.getTurnus().name()) //
		    .set(QCalendarSeries.calendarSeries.skipping, entrySerie.getSkipping()) //
		    .where(QCalendarSeries.calendarSeries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public EntrySerie getEntrySerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QCalendarSeries.calendarSeries.all()) //
		    .from(QCalendarSeries.calendarSeries) //
		    .where(QCalendarSeries.calendarSeries.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToEntrySerie(tuple);
	}
    }

    private EntrySerie convertTupleToEntrySerie(Tuple tuple) {
	long id = tuple.get(QCalendarSeries.calendarSeries.id);
	String type = tuple.get(QCalendarSeries.calendarSeries.type);
	String title = tuple.get(QCalendarSeries.calendarSeries.title);
	String description = tuple.get(QCalendarSeries.calendarSeries.description);
	LocalDateTime firstOccurrence = tuple.get(QCalendarSeries.calendarSeries.firstOccurrence).toLocalDateTime();
	LocalDate lastOccurence = tuple.get(QCalendarSeries.calendarSeries.lastOccurrence).toLocalDate();
	String timezone = tuple.get(QCalendarSeries.calendarSeries.timezone);

	int durationAmount = tuple.get(QCalendarSeries.calendarSeries.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QCalendarSeries.calendarSeries.durationUnit));

	int reminderAmount = tuple.get(QCalendarSeries.calendarSeries.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QCalendarSeries.calendarSeries.reminderUnit));
	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QCalendarSeries.calendarSeries.occupancy));
	Turnus turnus = Turnus.valueOf(Turnus.class, tuple.get(QCalendarSeries.calendarSeries.turnus));
	int skipping = tuple.get(QCalendarSeries.calendarSeries.skipping);
	return new EntrySerie(id, type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(firstOccurrence),
		CalendarDay.of(lastOccurence), timezone, CalendarTime.of(firstOccurrence), durationAmount, durationUnit,
		occupancy, turnus, skipping);
    }

    public boolean removeEntrySerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QCalendarSeries.calendarSeries) //
		    .where(QCalendarSeries.calendarSeries.id.eq(id)) //
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

    public Collection<Entry> getWeekEntries(int year, int week, String type) throws SQLException {
	LocalDate from = CalendarFactory.findWeek(year, week).getLocalDate();
	LocalDate to = from.plus(6, ChronoUnit.DAYS);
	return getEntriesBetween(type, LocalDateTime.of(from, LocalTime.of(0, 0, 0)),
		LocalDateTime.of(to, LocalTime.of(23, 59, 59)));
    }

    public Collection<Entry> getEntriesBetween(String type, LocalDateTime from, LocalDateTime to) throws SQLException {
	checkAndCreateSerieEntries(type, to.toLocalDate());
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QCalendarEntries.calendarEntries.all())
		    .from(QCalendarEntries.calendarEntries) //
		    .where(QCalendarEntries.calendarEntries.occurrence.between(Timestamp.valueOf(from),
			    Timestamp.valueOf(to)));
	    if (type != null) {
		select.where(QCalendarEntries.calendarEntries.type.eq(type));
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
	    SQLQuery<Tuple> seriesNeedingUpdate = queryFactory //
		    .select(QCalendarSeries.calendarSeries.all()) //
		    .from(QCalendarSeries.calendarSeries) //
		    .where(QCalendarSeries.calendarSeries.lastEntryCreated.before(Date.valueOf(to)));
	    if (type != null) {
		seriesNeedingUpdate.where(QCalendarSeries.calendarSeries.type.eq(type));
	    }
	    try (CloseableIterator<Tuple> iterator = seriesNeedingUpdate.iterate()) {
		while (iterator.hasNext()) {
		    Tuple tuple = iterator.next();
		    EntrySerie entrySerie = convertTupleToEntrySerie(tuple);
		    Date lastDate = tuple.get(QCalendarSeries.calendarSeries.lastEntryCreated);
		    createSerieEntries(queryFactory, entrySerie, lastDate.toLocalDate(), to);
		    queryFactory.commit();
		}
	    }
	}
    }

    private void createSerieEntries(SQLQueryFactory queryFactory, EntrySerie entrySerie, LocalDate from, LocalDate to)
	    throws SQLException {
	Turnus turnus = entrySerie.getTurnus();
	LocalDate currentDate = from.plus(turnus.getAmout(), turnus.getUnit());
	LocalDate lastDate = entrySerie.getLastDate() != null ? entrySerie.getLastDate().getLocalDate() : null;
	while ((currentDate.isBefore(to) || currentDate.isEqual(to)) && //
		((lastDate == null) || currentDate.isBefore(lastDate) || currentDate.isEqual(lastDate))) {
	    CalendarDay date = CalendarDay.of(currentDate);
	    Entry entry = new Entry(entrySerie.getType(), entrySerie.getTitle(), entrySerie.getDescription(),
		    entrySerie.getParticipants(), entrySerie.getReminder() != null ? true : false,
		    entrySerie.getReminder(), date, entrySerie.getTimezone(), entrySerie.getStartTime(),
		    entrySerie.getDurationAmount(), entrySerie.getDurationUnit(), entrySerie.getOccupancy());
	    insertEntry(entry);
	    currentDate = currentDate.plus(turnus.getAmout() + entrySerie.getSkipping(), turnus.getUnit());
	}
	SQLUpdateClause update = queryFactory.update(QCalendarSeries.calendarSeries)
		.set(QCalendarSeries.calendarSeries.lastEntryCreated, Date.valueOf(to))
		.where(QCalendarSeries.calendarSeries.id.eq(entrySerie.getId()));
	update.execute();
    }
}
