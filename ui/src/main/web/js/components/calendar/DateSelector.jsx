import React from 'react';
import { Link } from 'react-router';

import store from '../../flux/Store';

import CalendarController from '../../controller/CalendarController';
import YearSelector from './YearSelector';
import MonthSelector from './MonthSelector';
import SingleMonth from './SingleMonth';

export default class DateSelector extends React.Component {

    constructor( props ) {
        super( props );
        const calendar = store.getState().calendar;
        this.state = { year: calendar.year, month: calendar.month };
    }

    componentDidMount() {
        var component = this;
        CalendarController.getCalendar( this.state.year,
            function( calendar ) {
                component.setState( { calendarData: calendar });
            },
            function( response ) {
            }
        );
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        const calendar = store.getState().calendar;
        if ( ( calendar.year !== this.state.year ) || ( calendar.month !== this.state.month ) ) {
            this.setState( { year: calendar.year, month: calendar.month });
        }
    }

    render() {
        if ( this.state.calendarData ) {
            return <div className="card">
                <div className="card-header" style={{ margin: "0pt", padding: "5pt" }}>
                    <Link to={'/calendar/year/' + this.state.year}><YearSelector /></Link>                
                    <Link to={'/calendar/month/' + this.state.year + '/' + this.state.month }><MonthSelector name={true} /></Link>
                </div>
                <div className="card-block" style={{ margin: "0pt", padding: "0pt" }}>
                    <SingleMonth data={this.state.calendarData.months[this.state.month]} month={this.state.month} />
                </div>
            </div>;
        } else {
            return <div></div>;
        }
    }
}
