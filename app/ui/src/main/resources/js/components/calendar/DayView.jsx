import React from 'react';
import { browserHistory } from 'react-router';

import YearSelector from './YearSelector';
import MonthSelector from './MonthSelector';
import DaySelector from './DaySelector';

export default class DayView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired,
        month: React.PropTypes.string.isRequired,
        day: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { month: props.month, day: props.day, calendar: props.calendar };
        this.createAppointment = this.createAppointment.bind( this );
    }

    componentWillReceiveProps( nextProps ) {
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

    createAppointment( hour ) {
        var year = this.state.calendar.year;
        var month = this.state.month;
        if ( month < 10 ) {
            month = '0' + month;
        }
        var day = this.state.day;
        if ( day < 10 ) {
            day = '0' + day;
        }
        var hour2 = hour + 1;
        if ( hour < 10 ) {
            hour = '0' + hour;
        }
        if ( hour2 < 10 ) {
            hour2 = '0' + hour2;
        }
        browserHistory.push( '/dialog/calendar/create-appointment/' + year + '-' + month + '-' + day + '/' + hour + ':00:00' + '/' + hour2 + ':00:00' );
    }

    render() {
        var rows = [];
        for ( var i = 0; i <= 23; i++ ) {
            const hour = i;
            rows.push(
                <tr key={i} onClick={() => this.createAppointment( hour )}>
                    <th>{hour} h</th>
                    <td></td>
                </tr>
            );
        }
        return <div>
            <h1><DaySelector />.&nbsp;<MonthSelector />.&nbsp;<YearSelector /></h1>
            <table className="table table-hover">
                <thead className="thead-inverse">
                    <tr>
                        <th>Time</th>
                        <th>Appointments</th>
                    </tr>
                </thead>
                <tbody>
                    {rows}
                </tbody>
            </table>
        </div>;
    }
}