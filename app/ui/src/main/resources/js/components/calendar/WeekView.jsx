import React from 'react';

export default class WeekView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired,
        month: React.PropTypes.string.isRequired,
        day: React.PropTypes.string.isRequired
    };

    week = 0;
    monday = 0;
    sunday = 0;
    
    constructor( props ) {
        super( props );
        this.state = { month: props.month, day: props.day, calendar: props.calendar };
        this.setWeek();
    }

    componentWillReceiveProps( nextProps ) {
        if ( this.state.calendar != nextProps.calendar ) {
            this.setWeek();
            this.setState( { calendar: nextProps.calendar });
        }
        if ( this.state.month != nextProps.month ) {
            this.setWeek();
            this.setState( { month: nextProps.month });
        }
        if ( this.state.day != nextProps.day ) {
            this.setWeek();
            this.setState( { day: nextProps.day });
        }
    }

    setWeek() {
        var day = this.state.calendar.months[this.state.month].days[this.state.day];
        this.week = day.weekOfYear;
        var dayOfWeek = day.dayOfWeek;
        var startDay = day - (dayOfWeek -  1);
        var endDay = day + (7 - dayOfWeek);
    }
    
    render() {
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
            <h1>Week {this.week} {this.state.calendar.year}</h1>
            <table className="table table-hover">
                <thead className="thead-inverse">
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