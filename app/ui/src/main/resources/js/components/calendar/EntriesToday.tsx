import * as React from 'react';

import ApplicationComponent from '../ApplicationComponent';
import CalendarController from '../../controller/CalendarController';
import CalendarTime from '../../models/calendar/CalendarTime';
import CalendarEntryLabel from './CalendarEntryLabel';

export default class EntriesToday extends ApplicationComponent<any, any> {

    constructor( props: any ) {
        super( props );
        this.enableTooltips("root");
        this.state = { entries: [] };
    }

    private componentDidMount(): void {
        var component = this;
        CalendarController.getEntriesToday(
            this.props.type,
            function( entries ) {
                component.setState( { entries: entries });
            },
            function( response ) { }
        );
    }
    
    public render(): any {
        var rows = [];
        var i = 0;
        for ( var entry of this.state.entries ) {
            var date = entry.date;
            var time = CalendarTime.fromEntry( entry );
            rows.push(<li key={i} className="list-group-item"><CalendarEntryLabel entry={entry} showTime={true} /></li>);
            i++;
        }           
        return (
            <ul ref="root" className="list-group">
                {rows}                
            </ul>
        );
    }
}