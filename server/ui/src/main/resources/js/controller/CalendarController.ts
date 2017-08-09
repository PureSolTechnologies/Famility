import restController from './RESTController';

import CalendarEvent from '../models/calendar/CalendarEvent';
import EventType from '../models/calendar/EventType';
import DurationUnit from '../models/calendar/DurationUnit';
import TimeZoneInformation from '../models/calendar/TimeZoneInformation';

export default class CalendarController {

    static namesOfMonths = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];

    static getNameOfMonth( month: number ): string {
        return CalendarController.namesOfMonths[month - 1];
    }

    static getEventTypes( successfulCallback: ( types: EventType[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/event/types',
            null,
            function( response: XMLHttpRequest ) {
                var types: EventType[] = JSON.parse( response.response );
                successfulCallback( types );
            },
            errorCallback
        );
    }

    static getDurationUnits( successfulCallback: ( units: DurationUnit[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/duration-units',
            null,
            function( response: XMLHttpRequest ) {
                var units: DurationUnit[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getReminderDurationUnits( successfulCallback: ( units: DurationUnit[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/reminder-duration-units',
            null,
            function( response: XMLHttpRequest ) {
                var units: DurationUnit[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getTurnusUnits( successfulCallback: ( units: DurationUnit[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/turnus-units',
            null,
            function( response: XMLHttpRequest ) {
                var units: DurationUnit[] = JSON.parse( response.response );
                successfulCallback( units );
            },
            errorCallback
        );
    }

    static getCalendar( year: number, successfulCallback: ( calendar: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        var calendar: any = window.sessionStorage.getItem( 'calendarData.' + year );
        if ( calendar && calendar.months ) {
            successfulCallback( JSON.parse( calendar ) );
            return;
        }
        restController.GET( '/calendar/' + year,
            null,
            function( response: XMLHttpRequest ) {
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

    static getTimezones( successfulCallback: ( zones: TimeZoneInformation[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/timezones',
            null,
            function( response: XMLHttpRequest ) {
                var timezones: TimeZoneInformation[] = JSON.parse( response.response );
                successfulCallback( timezones );
            },
            errorCallback
        );
    }

    static createEvent( event: CalendarEvent, successfulCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.PUT( '/calendar/events',
            { "Content-Type": "application/json; charset=utf-8" },
            event,
            successfulCallback,
            errorCallback
        );
    }

    static getEvent( id: number, successfulCallback: ( response: any ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/' + id,
            { "Content-Type": "application/json; charset=utf-8" },
            function( response: XMLHttpRequest ) {
                var event: any = JSON.parse( response.response );
                successfulCallback( event );
            },
            errorCallback
        );
    }

    static createEventSerie( series: any, successfulCallback: ( response: XMLHttpRequest ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.PUT( '/calendar/series',
            { "Content-Type": "application/json; charset=utf-8" },
            series,
            successfulCallback, 
            errorCallback
        );
    }

    static createEventsResult( entity: any ): any[] {
        var events = JSON.parse( entity );
        events.months = [];
        for ( var i = 1; i <= 12; i++ ) {
            events.months[i] = [];
        }
        for ( var event of events ) {
            var month = event.beginDate.month;
            var dayOfMonth = event.beginDate.dayOfMonth;
            if ( !events.months[month][dayOfMonth] ) {
                events.months[month][dayOfMonth] = [];
            }
            events.months[month][dayOfMonth].push( event );
        }
        return events;
    }

    static getYearEvents( year: number, successCallback: ( events: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/year/' + year,
            null,
            function( response: XMLHttpRequest ) {
                var result: any[] = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getMonthEvents( year: number, month: number, successCallback: ( events: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/year/' + year + '/month/' + month,
            null,
            function( response: XMLHttpRequest ) {
                var result: any[] = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getDayEvents( year: number, month: number, day: number, successCallback: ( events: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/year/' + year + '/month/' + month + '/day/' + day,
            null,
            function( response: XMLHttpRequest ) {
                var result = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getWeekEvents( year: number, week: number, successCallback: ( events: any[] ) => void, errorCallback: ( response: XMLHttpRequest ) => void ): void {
        restController.GET( '/calendar/events/year/' + year + '/week/' + week,
            null,
            function( response: XMLHttpRequest ) {
                var result = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEventsToday( type: string, successCallback: ( events: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        var query: string = "";
        if ( type ) {
            query = '?type=' + type;
        }
        restController.GET( '/calendar/events/today' + query,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }

    static getEventsTomorrow( type: string, successCallback: ( events: any[] ) => void, errorCallback: ( response: any ) => void ): void {
        var query: string = "";
        if ( type ) {
            query = '?type=' + type;
        }
        restController.GET( '/calendar/events/tomorrow' + query,
            null,
            function( response: any ) {
                var result: any[] = CalendarController.createEventsResult( response.response );
                successCallback( result );
            },
            errorCallback
        );
    }
}

