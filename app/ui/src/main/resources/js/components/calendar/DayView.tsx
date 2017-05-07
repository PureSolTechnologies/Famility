import * as React from 'react';
import { browserHistory } from 'react-router';

import CalendarController from '../../controller/CalendarController';

export default class DayView extends React.Component<any, any> {

    static propTypes = {
        calendar: React.PropTypes.object.isRequired,
        month: React.PropTypes.number.isRequired,
        day: React.PropTypes.number.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { month: props.month, day: props.day, calendar: props.calendar, entries: [] };
        this.createEntry = this.createEntry.bind( this );
    }

    private componentDidMount() {
        let component = this;
        CalendarController.getDayEntries( this.state.calendar.year, this.state.month, this.state.day,
            function( entries ) {
                component.setState( { entries: entries });
            },
            function( response ) { });
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

    private createEntry( hour: number ) {
        let year: string = this.state.calendar.year;
        let month: string = this.state.month < 10 ? '0' + this.state.month : this.state.month;
        let day: string = this.state.day < 10 ? '0' + this.state.day : this.state.day;
        let hourString: string = hour < 10 ? '0' + hour : String( hour );
        let hourString2: string = hour + 1 < 10 ? '0' + ( hour + 1 ) : String( hour + 1 );
        browserHistory.push( '/dialog/calendar/create-entry/' + year + '-' + month + '-' + day + '/' + hourString + ':00:00' + '/' + hourString2 + ':00:00' );
    }

    private createTableRows(): any[] {
        let rows: any[] = []; 1
        for ( let i: number = 0; i <= 23; i++ ) {
            const hour: number = i;
            rows.push(
                <tr key={i} onClick={() => this.createEntry( hour )}>
                    <th>{hour} h</th>
                    {this.createTableRowEntry( hour )}
                </tr>
            );
        }
        return rows;
    }

    private createTableRowEntry( hour: number ): any {
        let content: any = {};
        return <td>{content}</td>;
    }

    render() {
        let rows = this.createTableRows();
        return <div>
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
    }
}