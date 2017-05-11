import * as React from 'react';

import AbstractEntriesView from './AbstractEntriesView';
import CalendarController from '../../controller/CalendarController';

export default class WeekView extends AbstractEntriesView<any, any> {

    static propTypes = {
        calendar: React.PropTypes.object.isRequired,
        week: React.PropTypes.number.isRequired,
    };

    private unsubscribeStore: any = null;

    constructor( props: any ) {
        super( props );
        this.state = {
            week: props.week,
            calendar: props.calendar,
            entries: []
        };
    }

    private componentDidMount() {
        const component: WeekView = this;
        CalendarController.getWeekEntries( this.state.calendar.year, this.state.week,
            function( entries ) {
                component.setState( { entries: entries });
            },
            function( response ) { });
    }

    private componentDidUpdate() {
        this.enableTooltip( "root" );
    }

    private componentWillReceiveProps( nextProps: any ) {
        let update: boolean = false;
        let newState: any = {};
        if ( this.state.calendar != nextProps.calendar ) {
            update = true;
            newState.calendar = nextProps.calendar;
        }
        if ( this.state.week != nextProps.week ) {
            update = true;
            newState.week = nextProps.week;
        }
        if ( update ) {
            this.setState( newState );
        }
    }

    private getDate( week: any, day: number ) {
        if ( !week[day] ) {
            return '';
        }
        return week[day].dayOfMonth + '.' + week[day].month + '.' + week[day].year;
    }

    private createTableRows( week: any[] ) {
        let weekData: any[] = this.state.calendar.weeks[this.state.week];
        var rows: any[] = [];
        for ( var hour = 0; hour <= 23; hour++ ) {
            let moEntries: any[] = this.findEntries( this.state.entries, weekData[1].month, weekData[1].dayOfMonth, hour );
            let tuEntries: any[] = this.findEntries( this.state.entries, weekData[2].month, weekData[2].dayOfMonth, hour );
            let weEntries: any[] = this.findEntries( this.state.entries, weekData[3].month, weekData[3].dayOfMonth, hour );
            let thEntries: any[] = this.findEntries( this.state.entries, weekData[4].month, weekData[4].dayOfMonth, hour );
            let frEntries: any[] = this.findEntries( this.state.entries, weekData[5].month, weekData[5].dayOfMonth, hour );
            let saEntries: any[] = this.findEntries( this.state.entries, weekData[6].month, weekData[6].dayOfMonth, hour );
            let suEntries: any[] = this.findEntries( this.state.entries, weekData[7].month, weekData[7].dayOfMonth, hour );
            rows.push(
                <tr key={hour}>
                    <th>{hour} h</th>
                    {this.createTableRowEntry( moEntries, this.state.calendar.year, weekData[1].month, weekData[1].dayOfMonth, hour )}
                    {this.createTableRowEntry( tuEntries, this.state.calendar.year, weekData[2].month, weekData[2].dayOfMonth, hour )}
                    {this.createTableRowEntry( weEntries, this.state.calendar.year, weekData[3].month, weekData[3].dayOfMonth, hour )}
                    {this.createTableRowEntry( thEntries, this.state.calendar.year, weekData[4].month, weekData[4].dayOfMonth, hour )}
                    {this.createTableRowEntry( frEntries, this.state.calendar.year, weekData[5].month, weekData[5].dayOfMonth, hour )}
                    {this.createTableRowEntry( saEntries, this.state.calendar.year, weekData[6].month, weekData[6].dayOfMonth, hour )}
                    {this.createTableRowEntry( suEntries, this.state.calendar.year, weekData[7].month, weekData[7].dayOfMonth, hour )}
                </tr>
            );
        }
        return rows;
    }

    render() {
        if ( this.state.calendar ) {
            let week: any[] = this.state.calendar.weeks[this.state.week];
            let rows = this.createTableRows( week );
            return <div ref="root">
                <table style={{ bgcolor: '#ff0000' }} className="table table-hover">
                    <thead className="thead-inverse">
                        <tr>
                            <th style={{ width: '9%' }}></th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 1 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 2 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 3 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 4 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 5 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 6 )}</th>
                            <th style={{ width: '13%' }}>{this.getDate( week, 7 )}</th>
                        </tr>
                        <tr>
                            <th>Time</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Saturday</th>
                            <th>Sunday</th>
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