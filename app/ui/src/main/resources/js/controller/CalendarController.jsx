import restController from './RESTController';

export default class CalendarController {

    static namesOfMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    static getNameOfMonth( month ) {
        return CalendarController.namesOfMonths[month - 1];
    }

    static getEntryTypes( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/entries/types',
            null,
            function( response ) {
                var types = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getDurationUnits( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/entries/duration-units',
            null,
            function( response ) {
                var units = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getReminderDurationUnits( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/entries/reminder-duration-units',
            null,
            function( response ) {
                var units = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getTurnusUnits( successfulCallback, errorCallback ) {
        restController.GET( '/calendar/entries/turnus-units',
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

    static createEntry( entry, successfulCallback, errorCallback ) {
        restController.PUT( '/calendar/entries',
            { "Content-Type": "application/json; charset=utf-8" },
            entry,
            successfulCallback,
            errorCallback
        );
    }

    static createEntrySerie( entrySerie, successfulCallback, errorCallback ) {
        restController.PUT( '/calendar/entrySeries',
            { "Content-Type": "application/json; charset=utf-8" },
            entrySerie,
            successfulCallback,
            errorCallback
        );
    }

    static createEntriesResult( entity ) {
        var entries = JSON.parse( entity );
        entries.months = [];
        for ( var i = 1; i <= 12; i++ ) {
            entries.months[i] = [];
        }
        for ( var entry of entries ) {
            var month = entry.date.month;
            var dayOfMonth = entry.date.dayOfMonth;
            if ( !entries.months[month][dayOfMonth] ) {
                entries.months[month][dayOfMonth] = [];
            }
            entries.months[month][dayOfMonth].push( entry );
        }
        return entries;
    }

    static getYearEntries( year, successCallback, errorCallback ) {
        restController.GET( '/calendar/entries/year/' + year,
            null,
            function( response ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getMonthEntries( year, month, successCallback, errorCallback ) {
        restController.GET( '/calendar/entries/year/' + year + '/month/' + month,
            null,
            function( response ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getDayEntries( year, month, day, successCallback, errorCallback ) {
        restController.GET( '/calendar/entries/year/' + year + '/month/' + month + '/day/' + day,
            null,
            function( response ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEntriesToday( successCallback, errorCallback ) {
        restController.GET( '/calendar/entries/today',
            null,
            function( response ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEntriesTomorrow( successCallback, errorCallback ) {
        restController.GET( '/calendar/entries/tomorrow',
            null,
            function( response ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }
}

