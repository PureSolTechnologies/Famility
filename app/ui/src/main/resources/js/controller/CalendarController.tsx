import restController from './RESTController';

export default class CalendarController {

    static namesOfMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    static getNameOfMonth( month: number ): string {
        return CalendarController.namesOfMonths[month - 1];
    }

    static getEntryTypes( successfulCallback: ( types: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/types',
            null,
            function( response: any ) {
                var types: any[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getDurationUnits( successfulCallback: ( units: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/duration-units',
            null,
            function( response: any ) {
                var units: any[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getReminderDurationUnits( successfulCallback: ( units: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/reminder-duration-units',
            null,
            function( response: any ) {
                var units: any[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getTurnusUnits( successfulCallback: ( units: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/turnus-units',
            null,
            function( response: any ) {
                var units: any[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getCalendar( year: number, successfulCallback: ( calendar: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        var calendar: any = window.sessionStorage.getItem( 'calendarData.' + year );
        if ( calendar && calendar.months) {
            successfulCallback( JSON.parse( calendar ) );
            return;
        }
        restController.GET( '/calendar/' + year,
            null,
            function( response: any ) {
                var calendar: any = JSON.parse( response.response );
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

    static getTimezones( successfulCallback: ( zones: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/timezones',
            null,
            function( response: any ) {
                var timezones: any[] = JSON.parse( response.response );
                successfulCallback( timezones );
            },
            errorCallback
        );
    }

    static createEntry( entry: any, successfulCallback: ( response: any ) => void, errorCallback: ( response: any ) => void ): void {
        restController.PUT( '/calendar/entries',
            { "Content-Type": "application/json; charset=utf-8" },
            entry,
            successfulCallback,
            errorCallback
        );
    }

    static createEntrySerie( entrySerie: any, successfulCallback: ( response: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.PUT( '/calendar/entrySeries',
            { "Content-Type": "application/json; charset=utf-8" },
            entrySerie,
            successfulCallback,
            errorCallback
        );
    }

    static createEntriesResult( entity: any ): any[] {
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

    static getYearEntries( year: number, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/year/' + year,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getMonthEntries( year: number, month: number, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/year/' + year + '/month/' + month,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getDayEntries( year: number, month: number, day: number, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/year/' + year + '/month/' + month + '/day/' + day,
            null,
            function( response: any ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getWeekEntries( year: number, week: number, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        restController.GET( '/calendar/entries/year/' + year + '/week/' + week,
            null,
            function( response: any ) {
                var result = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEntriesToday( type: string, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        var query: string = "";
        if ( type ) {
            query = '?type=' + type;
        }
        restController.GET( '/calendar/entries/today' + query,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEntriesTomorrow( type: string, successCallback: ( entries: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        var query: string = "";
        if ( type ) {
            query = '?type=' + type;
        }
        restController.GET( '/calendar/entries/tomorrow' + query,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEntriesResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }
}

