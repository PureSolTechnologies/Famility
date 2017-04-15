import restController from './RESTController';

export default class CalendarController {

    static namesOfMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    static getNameOfMonth( month ) {
        return CalendarController.namesOfMonths[month - 1];
    }

    static getDurationUnits( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/appointments/duration-units',
            null,
            function( response ) {
                var units = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getReminderDurationUnits( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/appointments/reminder-duration-units',
            null,
            function( response ) {
                var units = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getCalendar( year, successfulCallback, errorCallback ) {
        var calendar = window.sessionStorage.getItem( 'calendarData.' + year );
        if ( calendar ) {
            successfulCallback( JSON.parse( calendar ) );
            return;
        }
        restController.GET( '/calendar/year/' + year,
            null,
            function( response ) {
                var calendar = JSON.parse( response.response );
                calendar.weeks = {};
                for ( var month in calendar.months ) {
                    if ( calendar.months.hasOwnProperty( month ) ) {
                        var monthObject = calendar.months[month];
                        for ( var day in monthObject.days ) {
                            if ( monthObject.days.hasOwnProperty( day ) ) {
                                var dayObject = monthObject.days[day];
                                if ( !calendar.weeks[dayObject.weekOfYear] ) {
                                    calendar.weeks[dayObject.weekOfYear] = {};
                                }
                                calendar.weeks[dayObject.weekOfYear][dayObject.dayOfWeek] = dayObject;
                            }
                        }
                    }
                }
                window.sessionStorage.setItem( 'calendarData.' + year, JSON.stringify( calendar ) );
                successfulCallback( calendar );
            },
            errorCallback
        );
    }

    static getTimezones( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/timezones',
            null,
            function( response ) {
                var timezones = JSON.parse( response.response );
                successfulCallback( timezones );
            },
            errorCallback
        );
    }

    static createAppointment( appointment, successfulCallback, errorCallback ) {
        restController.PUT( '/calendar/appointments',
            { "Content-Type": "application/json; charset=utf-8" },
            appointment,
            successfulCallback,
            errorCallback
        );
    }

    static createAppointmentSerie( appointmentSerie, successfulCallback, errorCallback ) {
        restController.PUT( '/calendar/appointmentSeries',
            { "Content-Type": "application/json; charset=utf-8" },
            appointmentSerie,
            successfulCallback,
            errorCallback
        );
    }

    static getYearAppointment( year, successCallback, errorCallback ) {
        restController.GET( '/calendar/appointments/year/' + year,
            null,
            function( response ) {
                var appointments = JSON.parse( response.response );
                successCallback( appointments );
            },
            errorCallback
        );
    }
}

