import React from 'react';

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

    render() {
        var rows = [];
        for ( var i = 0; i <= 23; i++ ) {
            rows.push(
                <tr key={i}>
                    <th>{i} h</th>
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