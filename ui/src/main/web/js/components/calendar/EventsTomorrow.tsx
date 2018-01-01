import * as React from 'react';

import ApplicationComponent from '../ApplicationComponent';
import CalendarController from '../../controller/CalendarController';
import CalendarTime from '../../models/calendar/CalendarTime';
import CalendarEventLabel from './CalendarEventLabel';

export default class EventsTomorrow extends ApplicationComponent<any, any>  {

    constructor( props: any ) {
        super( props );
        this.enableTooltips("root");
        this.state = { events: [] };
    }

    private componentDidMount(): void {
        var component = this;
        CalendarController.getEventsTomorrow(
            this.props.type,
            function( events ) {
                component.setState( { events: events });
            },
            function( response ) { }
        );
    }
    
    public render(): any {
        var rows: any[] = [];
        var i = 0;
        for ( var event of this.state.events ) {
            var date = event.date;
            var time = CalendarTime.fromEvent( event );
            rows.push(<li key ={i} className="list-group-item"><CalendarEventLabel event={event} showTime={true} /></li>);
            i++;
        }
        return (
            <ul ref="root" className="list-group">
                {rows}                
            </ul>
        );
    }
}