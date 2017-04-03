package com.puresoltechnologies.lifeassist.app.impl.calendar;

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
import com.querydsl.sql.SQLExpressions;

public class CalendarManager {

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

    public void createAppointmentSerie(AppointmentSerie appointment) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    queryFactory.insert(QAppointmentSeries.appointmentSeries)//
		    .set(QAppointmentSeries.appointmentSeries.id, SQLExpressions.nextval("appointment_series_id_seq")) //
		    .set(QAppointmentSeries.appointmentSeries.title, appointment.getTitle()) //
		    .set(QAppointmentSeries.appointmentSeries.description, appointment.getDescription()) //
		    .set(QAppointmentSeries.appointmentSeries.startDate,
			    Date.valueOf(CalendarDay.toLocalDate(appointment.getStartDate()))) //
		    .set(QAppointmentSeries.appointmentSeries.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getFromTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getToTime()))) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeAmount, appointment.getTimeAmount()) //
		    .set(QAppointmentSeries.appointmentSeries.reminderTimeUnit, appointment.getTimeUnit().name()) //
		    .execute();
	    queryFactory.commit();
	}
    }

    public void createAppointment(Appointment appointment) throws SQLException {
	try (ExtendedSQLQueryFactory queryFactory = DatabaseConnector.createQueryFactory()) {
	    queryFactory.insert(QAppointments.appointments)//
		    .set(QAppointments.appointments.id, SQLExpressions.nextval("appointment_series_id_seq")) //
		    .set(QAppointments.appointments.title, appointment.getTitle()) //
		    .set(QAppointments.appointments.description, appointment.getDescription()) //
		    .set(QAppointments.appointments.date, Date.valueOf(CalendarDay.toLocalDate(appointment.getDate()))) //
		    .set(QAppointments.appointments.fromTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getFromTime()))) //
		    .set(QAppointments.appointments.toTime,
			    Time.valueOf(CalendarTime.toLocalTime(appointment.getToTime()))) //
		    .set(QAppointments.appointments.reminderTimeAmount, appointment.getTimeAmount()) //
		    .set(QAppointments.appointments.reminderTimeUnit, appointment.getTimeUnit().name()) //
		    .execute();
	    queryFactory.commit();
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
