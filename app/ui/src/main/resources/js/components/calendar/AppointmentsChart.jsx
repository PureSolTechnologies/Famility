import React from 'react';

export default class AppointmentsChart extends React.Component {

    static propTypes = {
        appointments: React.PropTypes.array,
        calendar: React.PropTypes.object
    };

    constructor( props ) {
        super( props );
        this.state = {calendar: props.calendar, appointments: props.appointments}
    }

    componentDidMount() {
        var chart = d3.select('#appointmentsChart');
        var width = chart.node().getBoundingClientRect().width;
        var svg = chart.append('svg').style('width', width).style('height', '75em');
        svg.append('circle').style('x', 0 ).style('y', 0 ).style('r', width );
    }
    
    render() {
        var svg = 
            <svg xmlns="http://www.w3.org/2000/svg">
            <title>Appointments</title>
            <rect x="0" y="0" width="10" height="10" />
            </svg>;
        return <div id="appointmentsChart">{svg}</div>;
    }
}
