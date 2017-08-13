import * as React from 'react';
import { browserHistory } from 'react-router';

import ApplicationComponent from '../ApplicationComponent';
import CalendarEventLabel from './CalendarEventLabel';

declare var $: any;

export default class AbstractEventsView<P, S> extends ApplicationComponent<P, S> {

    constructor( props: any ) {
        super( props );
    }

    protected findEvents( events: any, month: number, day: number, hour: number ): any[] {
        let foundEvents: any[] = [];
        if ( events.months && events.months[month] ) {
            let candidates: any[] = events.months[month][day];
            if ( candidates ) {
                for ( let candidate of candidates ) {
                    if ( candidate.beginTime.hour === hour ) {
                        foundEvents.push( candidate );
                    }
                }
            }
        }
        return foundEvents;
    }

    protected isNow( year: number, month: number, day: number, hour: number ): boolean {
        let now: any = new Date();
        if ( now.getYear() + 1900 != year ) {
            return false;
        }
        if ( now.getMonth() + 1 != month ) {
            return false;
        }
        if ( now.getDate() != day ) {
            return false;
        }
        if ( now.getHours() != hour ) {
            return false;
        }
        return true;
    }

    protected createTableRowEvent( events: any[], year: number, month: number, day: number, hour: number ): any {
        let style: any = this.isNow( year, month, day, hour ) ? { border: "solid red 2pt" } : {};
        if ( events.length == 0 ) {
            return <td style={style} onClick={() => this.createEvent( year, month, day, hour )}></td>;
        } else {
            return <td style={style}>{this.renderEvents( events )}</td>;
        }
    }

    protected createEvent( year: number, month: number, day: number, hour: number ) {
        let yearString: string = String( year );
        let monthString: string = month < 10 ? '0' + month : String( month );
        let dayString: string = day < 10 ? '0' + day : String( day );
        let hourStringFrom: string = hour < 10 ? '0' + hour : String( hour );
        let hourStringTo: string = hour + 1 < 10 ? '0' + ( hour + 1 ) : String( hour + 1 );
        browserHistory.push( '/dialog/calendar/create-event/' + yearString + '-' + monthString + '-' + dayString + '/' + hourStringFrom + ':00:00' + '/' + hourStringTo + ':00:00' );
    }

    /**
     * This method renders an array of elements representing the events of the calendar.
     */
    protected renderEvents( events: any[] ): any {
        let renderedEvents: any[] = [];
        let key: number = 0;
        for ( let event of events ) {
            renderedEvents.push( <CalendarEventLabel key={key} event={event} /> )
            key++;
        }
        return renderedEvents;
    }

}