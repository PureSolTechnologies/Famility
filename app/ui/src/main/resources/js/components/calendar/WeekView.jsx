import React from 'react';

export default class WeekView extends React.Component {

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

    getDate( week, day ) {
        var weekObject = this.state.calendar.weeks[week];
        if ( !weekObject[day] ) {
            return '';
        }
        return weekObject[day].dayOfMonth + '.' + weekObject[day].month + '.' + weekObject[day].year;
    }

    render() {
        var day = this.state.calendar.months[this.state.month].days[this.state.day];
        var week = day.weekOfYear;
        var rows = [];
        for ( var i = 0; i <= 23; i++ ) {
            rows.push(
                <tr key={i}>
                    <th>{i} h</th>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                    <td></td>
                </tr>
            );
        }
        return <div>
            <h1>Week {week} {this.state.calendar.year}</h1>
            <table className="table table-hover">
                <thead className="thead-inverse">
                    <tr>
                        <th></th>
                        <th>{this.getDate( week, 1 )}</th>
                        <th>{this.getDate( week, 2 )}</th>
                        <th>{this.getDate( week, 3 )}</th>
                        <th>{this.getDate( week, 4 )}</th>
                        <th>{this.getDate( week, 5 )}</th>
                        <th>{this.getDate( week, 6 )}</th>
                        <th>{this.getDate( week, 7 )}</th>
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
    }
}