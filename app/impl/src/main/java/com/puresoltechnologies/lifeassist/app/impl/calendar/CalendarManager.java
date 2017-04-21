package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.lang.reflect.Field;
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

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
import com.puresoltechnologies.lifeassist.app.api.calendar.TimeZoneInformation;
import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;
import com.puresoltechnologies.lifeassist.app.impl.db.ExtendedSQLQueryFactory;
import com.puresoltechnologies.lifeassist.app.model.QAppointmentSeries;
import com.puresoltechnologies.lifeassist.app.model.QAppointments;
import com.querydsl.core.Tuple;
import com.querydsl.sql.SQLExpressions;
import com.querydsl.sql.SQLQuery;

public class CalendarManager {

    private static final Field appointmentIdField;
    private static final Field appointmentSerieIdField;
    static {
	try {
	    appointmentIdField = Appointment.class.getDeclaredField("id");
	    appointmentIdField.setAccessible(true);
	    appointmentSerieIdField = AppointmentSerie.class.getDeclaredField("id");
	    appointmentSerieIdField.setAccessible(true);
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

    public AppointmentType[] getAppointmentTypes() {
	return AppointmentType.values();
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

    public long createAppointment(Appointment appointment) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(appointment.getDate());
	    LocalTime time = CalendarTime.toLocalTime(appointment.getTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, appointment.getZoneId());
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("appointments_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QAppointments.appointments)//
		    .set(QAppointments.appointments.id, id) //
		    .set(QAppointments.appointments.type, appointment.getType().name()) //
		    .set(QAppointments.appointments.title, appointment.getTitle()) //
		    .set(QAppointments.appointments.description, appointment.getDescription()) //
		    .set(QAppointments.appointments.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QAppointments.appointments.timezone, appointment.getTimezone()) //
		    .set(QAppointments.appointments.durationAmount, appointment.getDurationAmount()) //
		    .set(QAppointments.appointments.durationUnit, appointment.getDurationUnit().name()) //
		    .set(QAppointments.appointments.reminderAmount, appointment.getReminder().getAmount()) //
		    .set(QAppointments.appointments.reminderUnit, appointment.getReminder().getUnit().name()) //
		    .set(QAppointments.appointments.occupancy, appointment.getOccupancy().name()) //
		    .execute();
	    queryFactory.commit();
	    appointmentIdField.set(appointment, id);
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateAppointment(Appointment appointment) throws SQLException {
	return updateAppointment(appointment.getId(), appointment);
    }

    public boolean updateAppointment(long id, Appointment appointment) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(appointment.getDate());
	    LocalTime time = CalendarTime.toLocalTime(appointment.getTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, appointment.getZoneId());
	    long updated = queryFactory//
		    .update(QAppointments.appointments)//
		    .set(QAppointments.appointments.type, appointment.getType().name()) //
		    .set(QAppointments.appointments.title, appointment.getTitle()) //
		    .set(QAppointments.appointments.description, appointment.getDescription()) //
		    .set(QAppointments.appointments.occurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QAppointments.appointments.timezone, appointment.getTimezone()) //
		    .set(QAppointments.appointments.durationAmount, appointment.getDurationAmount()) //
		    .set(QAppointments.appointments.durationUnit, appointment.getDurationUnit().name()) //
		    .set(QAppointments.appointments.reminderAmount, appointment.getReminder().getAmount()) //
		    .set(QAppointments.appointments.reminderUnit, appointment.getReminder().getUnit().name()) //
		    .set(QAppointments.appointments.occupancy, appointment.getOccupancy().name()) //
		    .where(QAppointments.appointments.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public Appointment getAppointment(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QAppointments.appointments.all()) //
		    .from(QAppointments.appointments) //
		    .where(QAppointments.appointments.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return createAppointment(tuple);
	}
    }

    private Appointment createAppointment(Tuple tuple) {
	AppointmentType type = AppointmentType.valueOf(tuple.get(QAppointments.appointments.type));
	String title = tuple.get(QAppointments.appointments.title);
	String description = tuple.get(QAppointments.appointments.description);
	LocalDateTime occurrence = tuple.get(QAppointments.appointments.occurrence).toLocalDateTime();
	String timezone = tuple.get(QAppointments.appointments.timezone);

	int durationAmount = tuple.get(QAppointments.appointments.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QAppointments.appointments.durationUnit));

	int reminderAmount = tuple.get(QAppointments.appointments.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QAppointments.appointments.reminderUnit));

	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QAppointments.appointments.occupancy));
	return new Appointment(type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(occurrence), timezone,
		CalendarTime.of(occurrence), durationAmount, durationUnit, occupancy);
    }

    public boolean removeAppointment(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QAppointments.appointments) //
		    .where(QAppointments.appointments.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public long createAppointmentSerie(AppointmentSerie appointmentSerie) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(appointmentSerie.getStartDate());
	    LocalTime time = CalendarTime.toLocalTime(appointmentSerie.getStartTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, appointmentSerie.getZoneId());
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("appointment_series_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QAppointmentSeries.appointmentSeries)//
		    .set(QAppointmentSeries.appointmentSeries.id, id) //
		    .set(QAppointmentSeries.appointmentSeries.type, appointmentSerie.getType().name()) //
		    .set(QAppointmentSeries.appointmentSeries.title, appointmentSerie.getTitle()) //
		    .set(QAppointmentSeries.appointmentSeries.description, appointmentSerie.getDescription()) //
		    .set(QAppointmentSeries.appointmentSeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QAppointmentSeries.appointmentSeries.timezone, appointmentSerie.getTimezone()) //
		    .set(QAppointmentSeries.appointmentSeries.durationAmount, appointmentSerie.getDurationAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.durationUnit, appointmentSerie.getDurationUnit().name()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderAmount,
			    appointmentSerie.getReminder().getAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderUnit,
			    appointmentSerie.getReminder().getUnit().name()) //
		    .set(QAppointmentSeries.appointmentSeries.occupancy, appointmentSerie.getOccupancy().name()) //
		    .set(QAppointmentSeries.appointmentSeries.turnus, appointmentSerie.getTurnus().name()) //
		    .set(QAppointmentSeries.appointmentSeries.skipping, appointmentSerie.getSkipping()) //
		    .execute();
	    queryFactory.commit();
	    appointmentSerieIdField.set(appointmentSerie, id);
	    return id;
	} catch (IllegalArgumentException | IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }

    public boolean updateAppointmentSerie(AppointmentSerie appointmentSerie) throws SQLException {
	return updateAppointmentSerie(appointmentSerie.getId(), appointmentSerie);
    }

    public boolean updateAppointmentSerie(long id, AppointmentSerie appointmentSerie) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    LocalDate date = CalendarDay.toLocalDate(appointmentSerie.getStartDate());
	    LocalTime time = CalendarTime.toLocalTime(appointmentSerie.getStartTime());
	    ZonedDateTime occurrence = ZonedDateTime.of(date, time, appointmentSerie.getZoneId());
	    long updated = queryFactory//
		    .update(QAppointmentSeries.appointmentSeries)//
		    .set(QAppointmentSeries.appointmentSeries.type, appointmentSerie.getType().name()) //
		    .set(QAppointmentSeries.appointmentSeries.title, appointmentSerie.getTitle()) //
		    .set(QAppointmentSeries.appointmentSeries.description, appointmentSerie.getDescription()) //
		    .set(QAppointmentSeries.appointmentSeries.firstOccurrence, Timestamp.from(occurrence.toInstant())) //
		    .set(QAppointmentSeries.appointmentSeries.timezone, appointmentSerie.getTimezone()) //
		    .set(QAppointmentSeries.appointmentSeries.durationAmount, appointmentSerie.getDurationAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.durationUnit, appointmentSerie.getDurationUnit().name()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderAmount,
			    appointmentSerie.getReminder().getAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderUnit,
			    appointmentSerie.getReminder().getUnit().name()) //
		    .set(QAppointmentSeries.appointmentSeries.occupancy, appointmentSerie.getOccupancy().name()) //
		    .set(QAppointmentSeries.appointmentSeries.turnus, appointmentSerie.getTurnus().name()) //
		    .set(QAppointmentSeries.appointmentSeries.skipping, appointmentSerie.getSkipping()) //
		    .where(QAppointmentSeries.appointmentSeries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return updated > 0;
	}
    }

    public AppointmentSerie getAppointmentSerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> query = queryFactory//
		    .select(QAppointmentSeries.appointmentSeries.all()) //
		    .from(QAppointmentSeries.appointmentSeries) //
		    .where(QAppointmentSeries.appointmentSeries.id.eq(id));
	    Tuple tuple = query.fetchOne();
	    if (tuple == null) {
		return null;
	    }
	    return createAppointmentSeries(tuple);
	}
    }

    private AppointmentSerie createAppointmentSeries(Tuple tuple) {
	AppointmentType type = AppointmentType.valueOf(tuple.get(QAppointmentSeries.appointmentSeries.type));
	String title = tuple.get(QAppointmentSeries.appointmentSeries.title);
	String description = tuple.get(QAppointmentSeries.appointmentSeries.description);
	LocalDateTime firstOccurrence = tuple.get(QAppointmentSeries.appointmentSeries.firstOccurrence)
		.toLocalDateTime();
	String timezone = tuple.get(QAppointmentSeries.appointmentSeries.timezone);

	int durationAmount = tuple.get(QAppointmentSeries.appointmentSeries.durationAmount);
	ChronoUnit durationUnit = ChronoUnit.valueOf(tuple.get(QAppointments.appointments.durationUnit));

	int reminderAmount = tuple.get(QAppointmentSeries.appointmentSeries.reminderAmount);
	ChronoUnit reminderUnit = ChronoUnit.valueOf(tuple.get(QAppointmentSeries.appointmentSeries.reminderUnit));
	OccupancyStatus occupancy = OccupancyStatus.valueOf(tuple.get(QAppointmentSeries.appointmentSeries.occupancy));
	Turnus turnus = Turnus.valueOf(tuple.get(QAppointmentSeries.appointmentSeries.turnus));
	int skipping = tuple.get(QAppointmentSeries.appointmentSeries.skipping);
	return new AppointmentSerie(type, title, description, new ArrayList<>(), reminderAmount > 0,
		new Reminder(reminderAmount, reminderUnit), CalendarDay.of(firstOccurrence), timezone,
		CalendarTime.of(firstOccurrence), durationAmount, durationUnit, occupancy, turnus, skipping);
    }

    public boolean removeAppointmentSerie(long id) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    long deleted = queryFactory//
		    .delete(QAppointmentSeries.appointmentSeries) //
		    .where(QAppointmentSeries.appointmentSeries.id.eq(id)) //
		    .execute();
	    queryFactory.commit();
	    return deleted > 0;
	}
    }

    public Collection<Appointment> getAppointmentsToday() throws SQLException {
	LocalDate today = LocalDate.now();
	return getDayAppointments(today.getYear(), today.getMonth().getValue(), today.getDayOfMonth());
    }

    public Collection<Appointment> getAppointmentsTomorrow() throws SQLException {
	LocalDate today = LocalDate.now();
	LocalDate tomorrow = today.plusDays(1);
	return getDayAppointments(tomorrow.getYear(), tomorrow.getMonth().getValue(), tomorrow.getDayOfMonth());
    }

    public Collection<Appointment> getYearAppointments(int year) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QAppointments.appointments.all())
		    .from(QAppointments.appointments) //
		    .where(QAppointments.appointments.occurrence
			    .goe(Timestamp.valueOf(LocalDateTime.of(year, 1, 1, 0, 0))))
		    .where(QAppointments.appointments.occurrence
			    .loe(Timestamp.valueOf(LocalDateTime.of(year, 12, 31, 23, 59, 59))));
	    List<Appointment> appointments = new ArrayList<>();
	    for (Tuple tuple : select.fetch()) {
		appointments.add(createAppointment(tuple));
	    }
	    return appointments;
	}
    }

    public Collection<Appointment> getMonthAppointments(int year, int month) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    int lastDay = Month.of(month).length(Year.of(year).isLeap());
	    SQLQuery<Tuple> select = queryFactory.select(QAppointments.appointments.all())
		    .from(QAppointments.appointments) //
		    .where(QAppointments.appointments.occurrence
			    .goe(Timestamp.valueOf(LocalDateTime.of(year, month, 1, 0, 0))))
		    .where(QAppointments.appointments.occurrence
			    .loe(Timestamp.valueOf(LocalDateTime.of(year, month, lastDay, 23, 59, 59))));
	    List<Appointment> appointments = new ArrayList<>();
	    for (Tuple tuple : select.fetch()) {
		appointments.add(createAppointment(tuple));
	    }
	    return appointments;
	}
    }

    public Collection<Appointment> getDayAppointments(int year, int month, int day) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Tuple> select = queryFactory.select(QAppointments.appointments.all())
		    .from(QAppointments.appointments) //
		    .where(QAppointments.appointments.occurrence.between(
			    Timestamp.valueOf(LocalDateTime.of(year, month, day, 0, 0)),
			    Timestamp.valueOf(LocalDateTime.of(year, month, day, 23, 59, 59))));
	    List<Appointment> appointments = new ArrayList<>();
	    for (Tuple tuple : select.fetch()) {
		appointments.add(createAppointment(tuple));
	    }
	    return appointments;
	}
    }

}
