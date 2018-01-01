import * as React from 'react';
import { browserHistory } from 'react-router';
const { BellIcon, GiftIcon, TasklistIcon, SyncIcon, ClockIcon } = require( 'react-octicons' );

import ApplicationComponent from '../ApplicationComponent';
import EventIcon from './EventIcon';
import CalendarTime from '../../models/calendar/CalendarTime';

/**
 * This is a small component to show a calendar event in a short form.
 */
export default class CalendarEventLabel extends ApplicationComponent<any, undefined> {

    static propTypes = {
        event: React.PropTypes.object.isRequired,
        showTime: React.PropTypes.bool
    };

    constructor( props: any ) {
        super( props );
        this.enableTooltips( "span" );
        this.showEvent = this.showEvent.bind( this );
    }

    private showEvent(): void {
        browserHistory.push( '/dialog/calendar/show-event/' + this.props.event.id + "?origin=" + window.location.pathname );
    }

    protected isNow(): boolean {
        let now: any = new Date();
        let event: any = this.props.event;
        if ( now.getYear() + 1900 != event.beginDate.year ) {
            return false;
        }
        if ( now.getMonth() + 1 != event.beginDate.month ) {
            return false;
        }
        if ( now.getDate() != event.beginDate.dayOfMonth ) {
            return false;
        }
        if ( now.getHours() != event.beginTime.hour ) {
            return false;
        }
        return true;
    }

    render(): any {
        let event: any = this.props.event;
        let tooltip: string = "<h3>" + event.title + "</h3>";
        let date: any = event.beginDate;
        let time: CalendarTime = CalendarTime.fromEvent( event );
        tooltip += "(" + event.type + ")";
        tooltip += "<br/><i>" + date.year + "-" + date.month + "-" + date.dayOfMonth + " " + time.toString( false ) + " (" + event.durationAmount + ' ' + event.durationUnit + ")</i>";
        tooltip += "<br/><p>" + event.description + "</p>";
        let className: string = "alert-warning";
        if ( this.isNow() ) {
            className = "alert-d anger";
        }
        return (
            <span ref="span" className={className} data-toggle="tooltip" data-placement="bottom" data-html="true" title={tooltip} onClick={this.showEvent}>
                <EventIcon type={event.type} /> {this.props.showTime ? time.toString( false ) : null} {event.title}
            </span >
        );
    }

}