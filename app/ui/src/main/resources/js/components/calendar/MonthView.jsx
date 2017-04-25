import React from 'react';

import SingleMonth from './SingleMonth';
import CalendarController from '../../controller/CalendarController';

/**
 * This component shows a single month in calendar form.
 */
export default class MonthView extends React.Component {

    static propTypes = {
        year: React.PropTypes.number.isRequired,
        month: React.PropTypes.number.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { 
                month: props.month, 
                year: props.year, 
                entries: [] 
        };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getCalendar( this.state.year,
            function( calendar ) {
                component.setState( { calendarData: calendar });
                CalendarController.getMonthEntries( component.state.year, component.state.month,
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
        if ( (this.state.year != nextProps.year) || ( this.state.month != nextProps.month )) {
            this.state.year = nextProps.year;
            this.state.month = nextProps.month;
            this.componentDidMount();
        }
    }


    render() {
        if (this.state.calendarData) {
        return <div>
            <SingleMonth month={this.state.month} data={this.state.calendarData.months[this.state.month]} entries={this.state.entries.months ? this.state.entries.months[this.state.month] : []} />
        </div>;            
        } else {
            return <div>
        </div>;                        
        }
    }
}
