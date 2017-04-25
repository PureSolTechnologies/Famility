import React from 'react';
import { Link } from 'react-router';

import CalendarController from '../../controller/CalendarController';

import SingleMonth from './SingleMonth';

export default class YearView extends React.Component {

    static propTypes = {
        year: React.PropTypes.number.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { 
                year: props.year, 
                entries: [] 
        };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getCalendar( this.state.year,
            function( calendar ) {
                component.setState( { calendarData: calendar });
                CalendarController.getYearEntries( component.state.year,
                    function( entries ) {
                        component.setState( { entries: entries });
                    },
                    function( response ) { });
            },
            function( response ) {
            }
        );
    }

    componentWillReceiveProps( nextProps ) {
        if ( this.state.year != nextProps.year ) {
            this.state.year = nextProps.year;
            this.componentDidMount();
        }
    }

    render() {
        var rows = [];
        if ( this.state.calendarData ) {
            for ( var quartal = 1; quartal <= 4; quartal++ ) {
                var columns = [];
                for ( var monthInQuartal = 1; monthInQuartal <= 3; monthInQuartal++ ) {
                    var month = ( quartal - 1 ) * 3 + monthInQuartal;
                    columns.push( <div className="col-md-4" key={monthInQuartal}>
                        <div className="card">
                            <div className="card-header" style={{ margin: "0pt", padding: "5pt" }}>
                                <h3><Link to={'/calendar/month/' + this.state.calendarData.year + '/' + month} >{this.state.calendarData.months[month].name}</Link></h3>
                            </div>
                            <div className="card-block" style={{ margin: "0pt", padding: "0pt" }}>
                                <SingleMonth month={month} data={this.state.calendarData ? this.state.calendarData.months[month] : []} entries={this.state.entries && this.state.entries.months ? this.state.entries.months[month] : []} />
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
            {rows}
        </div>;
    }
}