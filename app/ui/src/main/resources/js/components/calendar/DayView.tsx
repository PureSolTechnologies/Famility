import * as React from 'react';

import AbstractEntriesView from './AbstractEntriesView';
import CalendarController from '../../controller/CalendarController';

declare var $: any;

export default class DayView extends AbstractEntriesView<any, any> {

    static propTypes = {
        calendar: React.PropTypes.object.isRequired,
        month: React.PropTypes.number.isRequired,
        day: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { month: props.month, day: props.day, calendar: props.calendar, entries: [] };
    }

    private componentDidMount() {
        let component: DayView = this;
        CalendarController.getDayEntries( this.state.calendar.year, this.state.month, this.state.day,
            function( entries ) {
                component.setState( { entries: entries });
            },
            function( response ) { });
    }

    private componentDidUpdate() {
        this.enableTooltip( "root" );
    }

    private componentWillReceiveProps( nextProps: any ) {
        if ( this.state.calendar != nextProps.calendar ) {
            this.setState( { calendar: nextProps.calendar });
        }
        if ( this.state.month != nextProps.month ) {
            this.setState( { month: nextProps.month });
        }
        if ( this.state.day != nextProps.day ) {
            this.setState( { day: nextProps.day });
        }
    }

    private createTableRows(): any[] {
        let rows: any[] = [];
        for ( let i: number = 0; i <= 23; i++ ) {
            const hour: number = i;
            let entries: any[] = this.findEntries( this.state.entries, this.state.month, this.state.day, hour );
            rows.push(
                <tr key={i} onClick={() => this.createEntry( this.state.calendar.year, this.state.month, this.state.day, hour )}>
                    <th>{hour} h</th>
                    {this.createTableRowEntry( entries, this.state.calendar.year, this.state.month, this.state.day, hour )}
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
                            <th>Entry</th>
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