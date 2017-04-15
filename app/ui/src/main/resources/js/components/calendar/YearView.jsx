import React from 'react';
import { Link } from 'react-router';

import CalendarController from '../../controller/CalendarController';

import SingleMonth from './SingleMonth';
import YearSelector from './YearSelector';

export default class YearView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { calendar: props.calendar, appointments: [] };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getYearAppointment( this.state.calendar.year,
            function( appointments ) {
                component.setState( { appointments: appointments });
            },
            function( response ) { });

    }

    componentWillReceiveProps( nextProps ) {
        if ( this.props.calendar != nextProps.calendar ) {
            this.setState( { calendar: nextProps.calendar });
        }
    }

    render() {
        var rows = [];
        if ( this.state.calendar ) {
            for ( var quartal = 1; quartal <= 4; quartal++ ) {
                var columns = [];
                for ( var monthInQuartal = 1; monthInQuartal <= 3; monthInQuartal++ ) {
                    var month = ( quartal - 1 ) * 3 + monthInQuartal;
                    columns.push( <div className="col-md-4" key={monthInQuartal}>
                        <div className="card">
                            <div className="card-header" style={{ margin: "0pt", padding: "5pt" }}>
                                <h3><Link to={'/calendar/month/' + this.state.calendar.year + '/' + month} >{this.state.calendar.months[month].name}</Link></h3>
                            </div>
                            <div className="card-block" style={{ margin: "0pt", padding: "0pt" }}>
                                <SingleMonth month={this.state.calendar.months[month].name} data={this.state.calendar.months[month]} />
                            </div>
                        </div>
                    </div> );
                }
                rows.push(
                    <div className="row" key={quartal}>
                        {columns}
                    </div>
                );
            }
        }
        return <div>
            <h1><YearSelector /></h1>
            {rows}
        </div>;
    }
}