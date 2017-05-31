package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
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
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.mysema.commons.lang.CloseableIterator;
import com.puresoltechnologies.lifeassist.app.api.calendar.DurationUnit;
import com.puresoltechnologies.lifeassist.app.api.calendar.Event;
import com.puresoltechnologies.lifeassist.app.api.calendar.EventType;
import com.puresoltechnologies.lifeassist.app.api.calendar.OccupancyStatus;
import com.puresoltechnologies.lifeassist.app.api.calendar.Reminder;
import com.puresoltechnologies.lifeassist.app.api.calendar.Series;
import com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.api.calendar.Turnus;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.calendar.QEventTypes;
import com.puresoltechnologies.lifeassist.app.model.calendar.QEvents;
import com.puresoltechnologies.lifeassist.app.model.calendar.QSeries;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import com.querydsl.sql.dml.SQLUpdateClause;

public class CalendarManager {

    private static final Field eventIdField;
    private static final Field serieIdField;
    static {
	try {
	    eventIdField = Event.class.getDeclaredField("id");
	    eventIdField.setAccessible(true);
	    serieIdField = Series.class.getDeclaredField("id");
	    serieIdField.setAccessible(true);
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

    public List<EventType> getEventTypes() throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory.select(QEventTypes.eventTypes.type, QEventTypes.eventTypes.name)
		    .from(QEventTypes.eventTypes);
	    List<EventType> eventTypes = new ArrayList<>();
	    for (Tuple tuple : query.fetch()) {
		String type = tuple.get(QEventTypes.eventTypes.type);
		String name = tuple.get(QEventTypes.eventTypes.name);
		eventTypes.add(new EventType(type, name));
	    }
	    return eventTypes;
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

    public long insertEvent(Event event) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("calendar.event_id_seq"));
	    long id = query.fetchOne();
	    ZonedDateTime begin = event.getBegin();
	    ZonedDateTime end = event.getEnd();
	    queryFactory//
		    .insert(QEvents.events) //
		    .set(QEvents.events.id, id) //
		    .set(QEvents.events.type, event.getType()) //
		    .set(QEvents.events.title, event.getTitle()) //
		    .set(QEvents.events.description, event.getDescription()) //
		    .set(QEvents.events.beginTime, Timestamp.from(begin.toInstant())) //
		    .set(QEvents.events.beginTimezone, begin.getZone().getId()) //
		    .set(QEvents.events.endTime, Timestamp.from(end.toInstant())) //
		    .set(QEvents.events.endTimezone, end.getZone().getId()) //
		    .set(QEvents.events.reminderAmount, event.getReminder().getAmount()) //
		    .set(QEvents.events.reminderUnit, event.getReminder().getUnit().name()) //
		    .set(QEvents.events.occupancy, event.getOccupancy().name())//
		    .execute();
	    queryFactory.commit();
	    eventIdField.set(event, id);
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateEvent(Event event) throws SQLException {
	return updateEvent(event.getId(), event);
    }

    public boolean updateEvent(long id, Event event) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    ZonedDateTime begin = event.getBegin();
	    ZonedDateTime end = event.getEnd();
	    long updated = queryFactory//
		    .update(QEvents.events)//
		    .set(QEvents.events.type, event.getType()) //
		    .set(QEvents.events.title, event.getTitle()) //
		    .set(QEvents.events.description, event.getDescription()) //
		    .set(QEvents.events.beginTime, Timestamp.from(begin.toInstant())) //
		    .set(QEvents.events.beginTimezone, begin.getZone().getId()) //
		    .set(QEvents.events.endTime, Timestamp.from(end.toInstant())) //
		    .set(QEvents.events.endTimezone, end.getZone().getId()) //
		    .set(QEvents.events.reminderAmount, event.getReminder().getAmount()) //
		    .set(QEvents.events.reminderUnit, event.getReminder().getUnit().name()) //
		    .set(QEvents.events.occupancy, event.getOccupancy().name()) //
		    .where(QEvents.events.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    eventIdField.set(event, id);
	    return updated > 0;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public Event getEvent(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QEvents.events.all()) //
		    .from(QEvents.events) //
		    .where(QEvents.events.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToEvent(tuple);
	}
    }

    private Event convertTupleToEvent(Tuple tuple) {
	long id = tuple.get(QEvents.events.id);
	String type = tuple.get(QEvents.events.type);
	String title = tuple.get(QEvents.events.title);
	String description = tuple.get(QEvents.events.description);
	LocalDateTime beginTime = tuple.get(QEvents.events.beginTime).toLocalDateTime();
	String beginTimezone = tuple.get(QEvents.events.beginTimezone);
	ZonedDateTime begin = ZonedDateTime.of(beginTime, ZoneId.of(beginTimezone));

	LocalDateTime endTime = tuple.get(QEvents.events.endTime).toLocalDateTime();
	String endTimezone = tuple.get(QEvents.events.endTimezone);
	ZonedDateTime end = ZonedDateTime.of(endTime, ZoneId.of(endTimezone));

	int reminderAmount = tuple.get(QEvents.events.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QEvents.events.reminderUnit));

	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QEvents.events.occupancy));
	return new Event(id, type, title, description, new ArrayList<>(),
		reminderAmount >= 0 ? new Reminder(reminderAmount, reminderUnit) : null, begin, end, occupancy);
    }

    public boolean removeEvent(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QEvents.events) //
		    .where(QEvents.events.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public long insertSeries(Series series) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("calendar.series_id_seq"));
	    long id = query.fetchOne();
	    ZonedDateTime firstOccurence = series.getFirstOccurence();
	    queryFactory//
		    .insert(QSeries.series)//
		    .set(QSeries.series.id, id) //
		    .set(QSeries.series.type, series.getType()) //
		    .set(QSeries.series.title, series.getTitle()) //
		    .set(QSeries.series.description, series.getDescription()) //
		    .set(QSeries.series.firstOccurrence, Timestamp.from(firstOccurence.toInstant())) //
		    .set(QSeries.series.lastOccurrence,
			    series.getLastOccurence() != null ? Date.valueOf(series.getLastOccurence()) : null) //
		    .set(QSeries.series.timezone, firstOccurence.getZone().getId()) //
		    .set(QSeries.series.durationAmount, series.getDurationAmount()) //
		    .set(QSeries.series.durationUnit, series.getDurationUnit().name()) //
		    .set(QSeries.series.reminderAmount, series.getReminder().getAmount()) //
		    .set(QSeries.series.reminderUnit, series.getReminder().getUnit().name()) //
		    .set(QSeries.series.occupancy, series.getOccupancy().name()) //
		    .set(QSeries.series.turnus, series.getTurnus().name()) //
		    .set(QSeries.series.skipping, series.getSkipping()) //
		    .set(QSeries.series.lastEventCreated,
			    Date.valueOf(series.getFirstOccurence().toLocalDate().minus(1, ChronoUnit.DAYS))) //
		    .execute();

	    Event event = new Event(series.getType(), series.getTitle(), series.getDescription(),
		    series.getParticipants(), series.getReminder(), series.getFirstOccurence(),
		    series.getFirstOccurence(), series.getOccupancy());
	    insertEvent(event);

	    queryFactory.commit();
	    serieIdField.set(series, id);

	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateSeries(Series series) throws SQLException {
	return updateSeries(series.getId(), series);
    }

    public boolean updateSeries(long id, Series series) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    ZonedDateTime firstOccurence = series.getFirstOccurence();
	    long updated = queryFactory//
		    .update(QSeries.series)//
		    .set(QSeries.series.type, series.getType()) //
		    .set(QSeries.series.title, series.getTitle()) //
		    .set(QSeries.series.description, series.getDescription()) //
		    .set(QSeries.series.firstOccurrence, Timestamp.from(firstOccurence.toInstant())) //
		    .set(QSeries.series.timezone, firstOccurence.getZone().getId()) //
		    .set(QSeries.series.durationAmount, series.getDurationAmount()) //
		    .set(QSeries.series.durationUnit, series.getDurationUnit().name()) //
		    .set(QSeries.series.reminderAmount, series.getReminder().getAmount()) //
		    .set(QSeries.series.reminderUnit, series.getReminder().getUnit().name()) //
		    .set(QSeries.series.occupancy, series.getOccupancy().name()) //
		    .set(QSeries.series.turnus, series.getTurnus().name()) //
		    .set(QSeries.series.skipping, series.getSkipping()) //
		    .where(QSeries.series.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    serieIdField.set(series, id);
	    return updated > 0;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public Series getSeries(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QSeries.series.all()) //
		    .from(QSeries.series) //
		    .where(QSeries.series.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return convertTupleToSeries(tuple);
	}
    }

    private Series convertTupleToSeries(Tuple tuple) {
	long id = tuple.get(QSeries.series.id);
	String type = tuple.get(QSeries.series.type);
	String title = tuple.get(QSeries.series.title);
	String description = tuple.get(QSeries.series.description);
	LocalDateTime localFirstOccurrence = tuple.get(QSeries.series.firstOccurrence).toLocalDateTime();
	Date lastOccuranceDate = tuple.get(QSeries.series.lastOccurrence);
	LocalDate lastOccurence = lastOccuranceDate != null ? lastOccuranceDate.toLocalDate() : null;
	String timezone = tuple.get(QSeries.series.timezone);

	ZonedDateTime firstOccurrence = ZonedDateTime.of(localFirstOccurrence, ZoneId.of(timezone));

	int durationAmount = tuple.get(QSeries.series.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QSeries.series.durationUnit));

	int reminderAmount = tuple.get(QSeries.series.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QSeries.series.reminderUnit));
	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QSeries.series.occupancy));
	Turnus turnus = Turnus.valueOf(Turnus.class, tuple.get(QSeries.series.turnus));
	int skipping = tuple.get(QSeries.series.skipping);
	return new Series(id, type, title, description, new ArrayList<>(), new Reminder(reminderAmount, reminderUnit),
		firstOccurrence, lastOccurence, durationAmount, durationUnit, occupancy, turnus, skipping);
    }

    public boolean removeSeries(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QSeries.series) //
		    .where(QSeries.series.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public Collection<Event> getEntriesToday(String type) throws SQLException {
	LocalDate today = LocalDate.now();
	return getDayEntries(today.getYear(), today.getMonth().getValue(), today.getDayOfMonth(), type);
    }

    public Collection<Event> getEntriesTomorrow(String type) throws SQLException {
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = today.plusDays(1);
	return getDayEntries(tomorrow.getYear(), tomorrow.getMonth().getValue(), tomorrow.getDayOfMonth(), type);
    }

    public Collection<Event> getYearEntries(int year, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, 1, 1, 0, 0);
	LocalDateTime to = LocalDateTime.of(year, 12, 31, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Event> getMonthEntries(int year, int month, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, month, 1, 0, 0);
	int lastDay = Month.of(month).length(Year.of(year).isLeap());
	LocalDateTime to = LocalDateTime.of(year, month, lastDay, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Event> getDayEntries(int year, int month, int day, String type) throws SQLException {
	LocalDateTime from = LocalDateTime.of(year, month, day, 0, 0);
	LocalDateTime to = LocalDateTime.of(year, month, day, 23, 59, 59);
	return getEntriesBetween(type, from, to);
    }

    public Collection<Event> getWeekEntries(int year, int week, String type) throws SQLException {
	LocalDate from = LocalDate.now() //
		.with(IsoFields.WEEK_BASED_YEAR, year) //
		.with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week) //
		.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
	LocalDate to = from.plus(6, ChronoUnit.DAYS);
	return getEntriesBetween(type, LocalDateTime.of(from, LocalTime.of(0, 0, 0)),
		LocalDateTime.of(to, LocalTime.of(23, 59, 59)));
    }

    public Collection<Event> getEntriesBetween(String type, LocalDateTime from, LocalDateTime to) throws SQLException {
	checkAndCreateSerieEntries(type, to.toLocalDate());
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QEvents.events.all()).from(QEvents.events) //
		    .where(QEvents.events.beginTime.between(Timestamp.valueOf(from), Timestamp.valueOf(to)));
	    if (type != null) {
		select.where(QEvents.events.type.eq(type));
	    }
	    List<Event> entries = new ArrayList<>();
	    for (Tuple tuple : select.fetch()) {
		entries.add(convertTupleToEvent(tuple));
	    }
	    return entries;
	}
    }

    private void checkAndCreateSerieEntries(String type, LocalDate to) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> seriesNeedingUpdate = queryFactory //
		    .select(QSeries.series.all()) //
		    .from(QSeries.series) //
		    .where(QSeries.series.lastEventCreated.before(Date.valueOf(to)));
	    if (type != null) {
		seriesNeedingUpdate.where(QSeries.series.type.eq(type));
	    }
	    try (CloseableIterator<Tuple> iterator = seriesNeedingUpdate.iterate()) {
		while (iterator.hasNext()) {
		    Tuple tuple = iterator.next();
		    Series series = convertTupleToSeries(tuple);
		    Date lastDate = tuple.get(QSeries.series.lastEventCreated);
		    createSeriesEntries(queryFactory, series, lastDate.toLocalDate(), to);
		    queryFactory.commit();
		}
	    }
	}
    }

    private void createSeriesEntries(SQLQueryFactory queryFactory, Series series, LocalDate from, LocalDate to)
	    throws SQLException {
	Turnus turnus = series.getTurnus();
	LocalDate currentDate = from.plus(turnus.getAmout(), turnus.getUnit());
	ZonedDateTime firstOccurence = series.getFirstOccurence();
	LocalTime time = firstOccurence.toLocalTime();
	ZoneId zone = firstOccurence.getZone();
	LocalDate lastInsertedDate = null;
	LocalDate lastOccurence = series.getLastOccurence();
	while ((currentDate.isBefore(to) || currentDate.isEqual(to)) && //
		((lastOccurence == null) || currentDate.isBefore(lastOccurence)
			|| currentDate.isEqual(lastOccurence))) {
	    ZonedDateTime begin = ZonedDateTime.of(currentDate, time, zone);
	    ZonedDateTime end = begin.plus(series.getDurationAmount(), series.getDurationUnit());
	    Event event = new Event(series.getType(), series.getTitle(), series.getDescription(),
		    series.getParticipants(), series.getReminder(), begin, end, series.getOccupancy());
	    insertEvent(event);
	    lastInsertedDate = currentDate;
	    currentDate = currentDate.plus(turnus.getAmout() + series.getSkipping(), turnus.getUnit());
	}
	if (lastInsertedDate != null) {
	    SQLUpdateClause update = queryFactory.update(QSeries.series)
		    .set(QSeries.series.lastEventCreated, Date.valueOf(lastInsertedDate))
		    .where(QSeries.series.id.eq(series.getId()));
	    update.execute();
	}
    }

}
