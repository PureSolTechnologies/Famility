import React from 'react';
import { Link } from 'react-router';

export default class SingleMonth extends React.Component {

    static propTypes = {
        month: React.PropTypes.number.isRequired,
        data: React.PropTypes.object.isRequired,
        appointments: React.PropTypes.array,
    };
    constructor( props ) {
        super( props );
        this.state = { month: props.month, data: props.data, apointments: props.appointments };
    }

    componentWillReceiveProps( nextProps ) {
        var newState = {};
        if ( this.state.month != nextProps.month ) {
            newState.month = nextProps.month;
        }
        if ( this.state.data != nextProps.data ) {
            newState.data = nextProps.data;
        }
        if ( this.state.appointments != nextProps.appointments ) {
            newState.appointments = nextProps.appointments;
        }
        this.setState( newState );
    }

    render() {
        var weeks = [];
        var days = this.state.data.days;
        var startWeek = days["1"]["weekOfYear"];
        var endWeek = days[Object.keys( days ).length]["weekOfYear"];
        if ( startWeek > endWeek ) {
            startWeek = 0;
        }
        var dayId = 1;
        var day = days[dayId];
        for ( var week = startWeek; week <= endWeek; week++ ) {
            var daysRow = [];
            for ( var dow = 1; dow <= 7; dow++ ) {
                if ( ( day ) && ( dow < day["dayOfWeek"] ) ) {
                    daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                } else {
                    if ( day ) {
                        if ( ( this.state.appointments ) && ( this.state.appointments[dayId] ) ) {
                            daysRow.push( <td key={daysRow.length} className="btn-warning"><Link to={'/calendar/day/' + this.state.data.year + '/' + this.state.data.month + '/' + dayId}>{dayId}</Link></td> );
                        } else {
                            daysRow.push( <td key={daysRow.length}><Link to={'/calendar/day/' + this.state.data.year + '/' + this.state.data.month + '/' + dayId}>{dayId}</Link></td> );
                        }
                    } else {
                        daysRow.push( <td key={daysRow.length}>&nbsp;</td> );
                    }
                    dayId++;
                    day = days[dayId];
                }
            }
            weeks.push(
                <tr key={week}>
                    <th style={{ borderRight: '1pt solid #000000' }}><Link to={'/calendar/week/' + this.state.data.year + '/' + week}>{week > 0 ? week : ''}</Link></th>
                    {daysRow}
                </tr>
            );
        }
        return <table width="100%" style={{ margin: "0pt", spacing: "0pt", border: "1pt solid  gray" }}>
            <thead style={{ border: "1pt solid  gray" }}>
                <tr>
                    <th></th>
                    <th>M</th>
                    <th>T</th>
                    <th>W</th>
                    <th>T</th>
                    <th>F</th>
                    <th>S</th>
                    <th>S</th>
                </tr>
            </thead>
            <tbody>
                {weeks}
            </tbody>
        </table>;
    }
}