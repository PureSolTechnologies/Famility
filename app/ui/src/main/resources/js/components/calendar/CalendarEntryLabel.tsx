import * as React from 'react';
import { browserHistory } from 'react-router';
const { BellIcon, GiftIcon, TasklistIcon, SyncIcon, ClockIcon  } = require( 'react-octicons' );

import ApplicationComponent from '../ApplicationComponent';
import CalendarTime from '../../models/calendar/CalendarTime';

/**
 * This is a small component to show a calendar entry in a short form.
 */
export default class CalendarEntryLabel extends ApplicationComponent<any, undefined> {

    static propTypes = {
        entry: React.PropTypes.object.isRequired,
        showTime: React.PropTypes.bool
    };

    constructor( props: any ) {
        super( props );
        this.showEntry = this.showEntry.bind( this );
    }

    private componentDidUpdate() {
        this.enableTooltip( "span" );
    }

    private showEntry() {
        browserHistory.push( '/dialog/calendar/show-entry/' + this.props.entry.id + "?origin=" + window.location.pathname );
    }
    
    protected isNow(): boolean {
        let now: any = new Date();
        if ( now.getYear() + 1900 != this.props.entry.date.year ) {
            return false;
        }
        if ( now.getMonth() + 1 != this.props.entry.date.month ) {
            return false;
        }
        if ( now.getDate() != this.props.entry.date.dayOfMonth ) {
            return false;
        }
        if ( now.getHours() != this.props.entry.time.hour ) {
            return false;
        }
        return true;
    }

    render(): any {
        let entry: any = this.props.entry;
        let tooltip: string = "<h3>" + entry.title + "</h3>";
        let date: any = entry.date;
        let time: CalendarTime = CalendarTime.fromEntry( entry );
        let icon: any = undefined;
        switch ( entry.type ) {
            case "reminder":
                icon = <BellIcon />;
                break;
            case "birthday":
                icon = <GiftIcon />;
                break;
            case "todo":
                icon = <TasklistIcon />;
                break;
            case "appointment":
                icon = <ClockIcon />;
                break;
            case "anniversary":
                icon = <SyncIcon />;
                break;
        }
        tooltip += "(" + entry.type + ")";
        tooltip += "<br/><i>" + date.year + "-" + date.month + "-" + date.dayOfMonth + " " + time.toString( false ) + " (" + entry.durationAmount + ' ' + entry.durationUnit + ")</i>";
        tooltip += "<br/><p>" + entry.description + "</p>";
        let className: string = "alert-warning";
        if (this.isNow()) {
            className = "alert-danger";
        }
        return (
            <span ref="span" className={className} data-toggle="tooltip" data-placement="bottom" data-html="true" title={tooltip} onClick={this.showEntry}>
                {icon} {this.props.showTime ? time.toString( false ) : null} {entry.title}
            </span >
        );
    }

}