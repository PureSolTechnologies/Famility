import React from 'react';

import CalendarController from '../../controller/CalendarController';
import CalendarTime from '../../models/calendar/CalendarTime';

export default class AppointmentsTomorrow extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { appointments: [] };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getAppointmentsTomorrow(
            function( appointments ) {
                component.setState( { appointments: appointments });
            },
            function( response ) { }
        );
    }

    render() {
        var rows = [];
        var i = 0;
        for ( var appointment of this.state.appointments ) {
            var date = appointment.date;
            var time = CalendarTime.fromAppointment( appointment );
            rows.push( <tr key={i}><th data-toggle="tooltip" data-delay="0" data-placement="bottom" title={appointment.durationAmount + ' ' +appointment.durationUnit}>{time.toString(false)} Uhr</th><td>{appointment.title} : {appointment.description}</td></tr> );
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