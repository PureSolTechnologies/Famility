import React from 'react';

export default class AppointmentsChart extends React.Component {

    static propTypes = {
        appointments: React.PropTypes.array.required,
        calendar: React.PropTypes.object.required
    };

    constructor( props ) {
        super( props );
    }

    componentDidMount() {
        var chart = d3.select('#appointmentsChart');
    }
    
    render() {
        return <div id="appointmentsChart"><h1>Appointments Chart</h1></div>;
    }
}
