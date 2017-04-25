import React from 'react';

import CalendarController from '../../controller/CalendarController';
import CalendarTime from '../../models/calendar/CalendarTime';

export default class EntriesToday extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { entries: [] };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getEntriesToday(
            function( entries ) {
                component.setState( { entries: entries });
            },
            function( response ) { }
        );
    }

    render() {
        var rows = [];
        var i = 0;
        for ( var entry of this.state.entries ) {
            var date = entry.date;
            var time = CalendarTime.fromEntry( entry );
            rows.push( <tr key={i}>
                <th data-toggle="tooltip" data-delay="0" data-placement="bottom" title={entry.durationAmount + ' ' + entry.durationUnit}>{time.toString( false )} Uhr</th>
                <td>{entry.title} : {entry.description}</td>
            </tr> );
            i++;
        }
        return (
            <table>
                <tbody>
                    {rows}
                </tbody>
            </table>
        );
    }
}