package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarDay;
import com.puresoltechnologies.lifeassist.app.api.calendar.CalendarTime;
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

    public List<TimeUnit> getTimeUnits() {
	List<TimeUnit> timeUnits = new ArrayList<>();
	timeUnits.add(TimeUnit.MINUTES);
	timeUnits.add(TimeUnit.HOURS);
	timeUnits.add(TimeUnit.DAYS);
	return timeUnits;
    }

    public AppointmentType[] getAppointmentTypes() {
	return AppointmentType.values();
    }

    public long createAppointment(Appointment appointment) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("appointment_series_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QAppointments.appointments)//
		    .set(QAppointments.appointments.id, id) //
		    .set(QAppointments.appointments.type, appointment.getType().name()) //
		    .set(QAppointments.appointments.title, appointment.getTitle()) //
		    .set(QAppointments.appointments.description, appointment.getDescription()) //
		    .set(QAppointments.appointments.date, Date.valueOf(CalendarDay.toLocalDate(appointment.getDate()))) //
		    .set(QAppointments.appointments.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getFromTime()))) //
		    .set(QAppointments.appointments.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getToTime()))) //
		    .set(QAppointments.appointments.reminderTimeAmount, appointment.getTimeAmount()) //
		    .set(QAppointments.appointments.reminderTimeUnit, appointment.getTimeUnit().name()) //
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
	    long updated = queryFactory//
		    .update(QAppointments.appointments)//
		    .set(QAppointments.appointments.type, appointment.getType().name()) //
		    .set(QAppointments.appointments.title, appointment.getTitle()) //
		    .set(QAppointments.appointments.description, appointment.getDescription()) //
		    .set(QAppointments.appointments.date, Date.valueOf(CalendarDay.toLocalDate(appointment.getDate()))) //
		    .set(QAppointments.appointments.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getFromTime()))) //
		    .set(QAppointments.appointments.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getToTime()))) //
		    .set(QAppointments.appointments.reminderTimeAmount, appointment.getTimeAmount()) //
		    .set(QAppointments.appointments.reminderTimeUnit, appointment.getTimeUnit().name()) //
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
	    Tuple appointment = query.fetchOne();
	    if (appointment == null) {
		return null;
	    }
	    AppointmentType type = AppointmentType.valueOf(appointment.get(QAppointments.appointments.type));
	    String title = appointment.get(QAppointments.appointments.title);
	    String description = appointment.get(QAppointments.appointments.description);
	    Integer timeAmount = appointment.get(QAppointments.appointments.reminderTimeAmount);
	    TimeUnit timeUnit = TimeUnit.valueOf(appointment.get(QAppointments.appointments.reminderTimeUnit));
	    CalendarDay date = CalendarDay.of(appointment.get(QAppointments.appointments.date).toLocalDate());
	    CalendarTime fromTime = CalendarTime.of(appointment.get(QAppointments.appointments.fromTime).toLocalTime());
	    CalendarTime toTime = CalendarTime.of(appointment.get(QAppointments.appointments.toTime).toLocalTime());
	    OccupancyStatus occupancy = OccupancyStatus.valueOf(appointment.get(QAppointments.appointments.occupancy));
	    return new Appointment(type, title, description, new ArrayList<>(), timeAmount != null, timeAmount,
		    timeUnit, date, fromTime, toTime, occupancy);
	}
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
	    SQLQuery<Long> query = queryFactory.select(SQLExpressions.nextval("appointment_series_id_seq"));
	    long id = query.fetchOne();
	    queryFactory//
		    .insert(QAppointmentSeries.appointmentSeries)//
		    .set(QAppointmentSeries.appointmentSeries.id, id) //
		    .set(QAppointmentSeries.appointmentSeries.type, appointmentSerie.getType().name()) //
		    .set(QAppointmentSeries.appointmentSeries.title, appointmentSerie.getTitle()) //
		    .set(QAppointmentSeries.appointmentSeries.description, appointmentSerie.getDescription()) //
		    .set(QAppointmentSeries.appointmentSeries.startDate,
			    Date.valueOf(CalendarDay.toLocalDate(appointmentSerie.getStartDate()))) //
		    .set(QAppointmentSeries.appointmentSeries.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointmentSerie.getFromTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointmentSerie.getToTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeAmount, appointmentSerie.getTimeAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeUnit, appointmentSerie.getTimeUnit().name()) //
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
	    long updated = queryFactory//
		    .update(QAppointmentSeries.appointmentSeries)//
		    .set(QAppointmentSeries.appointmentSeries.type, appointmentSerie.getType().name()) //
		    .set(QAppointmentSeries.appointmentSeries.title, appointmentSerie.getTitle()) //
		    .set(QAppointmentSeries.appointmentSeries.description, appointmentSerie.getDescription()) //
		    .set(QAppointmentSeries.appointmentSeries.startDate,
			    Date.valueOf(CalendarDay.toLocalDate(appointmentSerie.getStartDate()))) //
		    .set(QAppointmentSeries.appointmentSeries.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointmentSerie.getFromTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointmentSerie.getToTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeAmount, appointmentSerie.getTimeAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeUnit, appointmentSerie.getTimeUnit().name()) //
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
	    Tuple appointment = query.fetchOne();
	    if (appointment == null) {
		return null;
	    }
	    AppointmentType type = AppointmentType.valueOf(appointment.get(QAppointmentSeries.appointmentSeries.type));
	    String title = appointment.get(QAppointmentSeries.appointmentSeries.title);
	    String description = appointment.get(QAppointmentSeries.appointmentSeries.description);
	    Integer timeAmount = appointment.get(QAppointmentSeries.appointmentSeries.reminderTimeAmount);
	    TimeUnit timeUnit = TimeUnit
		    .valueOf(appointment.get(QAppointmentSeries.appointmentSeries.reminderTimeUnit));
	    CalendarDay startDate = CalendarDay
		    .of(appointment.get(QAppointmentSeries.appointmentSeries.startDate).toLocalDate());
	    CalendarTime fromTime = CalendarTime
		    .of(appointment.get(QAppointmentSeries.appointmentSeries.fromTime).toLocalTime());
	    CalendarTime toTime = CalendarTime
		    .of(appointment.get(QAppointmentSeries.appointmentSeries.toTime).toLocalTime());
	    OccupancyStatus occupancy = OccupancyStatus
		    .valueOf(appointment.get(QAppointmentSeries.appointmentSeries.occupancy));
	    Turnus turnus = Turnus.valueOf(appointment.get(QAppointmentSeries.appointmentSeries.turnus));
	    int skipping = appointment.get(QAppointmentSeries.appointmentSeries.skipping);
	    return new AppointmentSerie(type, title, description, new ArrayList<>(), timeAmount != null, timeAmount,
		    timeUnit, startDate, fromTime, toTime, occupancy, turnus, skipping);
	}
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

    public Collection<Appointment> getYearAppointments(int year) {
	// TODO Auto-generated method stub
	return null;
    }

    public Collection<Appointment> getMonthAppointments(int year, int month) {
	// TODO Auto-generated method stub
	return null;
    }

    public Collection<Appointment> getDayAppointments(int year, int month, int day) {
	// TODO Auto-generated method stub
	return null;
    }

}
