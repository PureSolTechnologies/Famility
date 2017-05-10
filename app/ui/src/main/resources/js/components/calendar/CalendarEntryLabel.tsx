import * as React from 'react';
import { browserHistory } from 'react-router';

import ApplicationComponent from '../ApplicationComponent';

/**
 * This is a small component to show a calendar entry in a short form.
 */
export default class CalendarEntryLabel extends ApplicationComponent<any, undefined> {

    static propTypes = {
        entry: React.PropTypes.object
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

    render(): any {
        let entry: any = this.props.entry;
        let tooltip: string = "<h3>" + entry.title + "</h3>";
        let date: any = entry.date;
        let time: any = entry.time;
        tooltip += "(" + entry.type + ")";
        tooltip += "<br/><i>" + date.year + "-" + date.month + "-" + date.dayOfMonth + " " + time.hour + ":" + time.minute + ":" + time.second + "</i>";
        tooltip += "<br/><p>" + entry.description + "</p>";
        return (
            <span ref="span" data-toggle="tooltip" data-placement="bottom" data-html="true" title={tooltip} style={{ border: "solid black 1pt" }} onClick={this.showEntry}>
                {entry.type} : {entry.title}
            </span >
        );
    }

}