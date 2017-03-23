package com.puresoltechnologies.lifeassist.app.impl.calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.puresoltechnologies.lifeassist.app.impl.db.DatabaseConnector;

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

    public void createAppointment(Appointment appointment) throws SQLException {
	try (Connection connection = DatabaseConnector.getConnection()) {
	    if (appointment.isRecurring()) {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO APPOINTMENTS (id, title, description, date, from_time, to_time, reminder_time_amount, reminder_time_unit)");
		statement.execute();
		connection.commit();
	    } else {
		PreparedStatement statement = connection.prepareStatement(
			"INSERT INTO APPOINTMENTS (id, title, description, date, from_time, to_time, reminder_time_amount, reminder_time_unit)");
		statement.execute();
		connection.commit();
	    }
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
