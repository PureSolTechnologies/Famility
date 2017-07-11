import * as React from 'react';

import AbstractEventsView from './AbstractEventsView';
import CalendarController from '../../controller/CalendarController';

declare var $: any;

export default class DayView extends AbstractEventsView<any, any> {

    static propTypes = {
        calendar: React.PropTypes.object.isRequired,
        month: React.PropTypes.number.isRequired,
        day: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.enableTooltips("root");
        this.state = { month: props.month, day: props.day, calendar: props.calendar, events: [] };
    }

    private componentDidMount() {
        let component: DayView = this;
        CalendarController.getDayEvents( this.state.calendar.year, this.state.month, this.state.day,
            function( events ) {
                component.setState( { events: events });
            },
            function( response ) { });
    }

    private componentWillReceiveProps( nextProps: any ) {
        let update: boolean = false;
        let newState: any = {};
        if ( this.state.calendar != nextProps.calendar ) {
            update = true;
            newState.calendar = nextProps.calendar;
        }
        if ( this.state.month != nextProps.month ) {
            update = true;
            newState.month = nextProps.month;
        }
        if ( this.state.day != nextProps.day ) {
            update = true;
            newState.day = nextProps.day;
        }
        if ( update ) {
            this.setState( newState );
        }
    }

    private createTableRows(): any[] {
        let rows: any[] = [];
        for ( let i: number = 0; i <= 23; i++ ) {
            const hour: number = i;
            let events: any[] = this.findEvents( this.state.events, this.state.month, this.state.day, hour );
            rows.push(
                <tr key={i}>
                    <th>{hour} h</th>
                    {this.createTableRowEvent( events, this.state.calendar.year, this.state.month, this.state.day, hour )}
                </tr>
            );
        }
        return rows;
    }

    render() {
        if ( this.state.calendar ) {
            let rows = this.createTableRows();
            return <div ref="root">
                <table className="table table-hover">
                    <thead className="thead-inverse">
                        <tr>
                            <th>Time</th>
                            <th>Event</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rows}
                    </tbody>
                </table>
            </div>;
        } else {
            return <div></div>;
        }
    }
}