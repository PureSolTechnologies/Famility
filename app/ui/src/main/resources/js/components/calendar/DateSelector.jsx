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
            return <div>
                <Link to={'/calendar/year/' + this.state.year}><h2><YearSelector /></h2></Link>                
                <Link to={'/calendar/month/' + this.state.year + '/' + this.state.month }><h3><MonthSelector name={true} /> </h3></Link>
                <SingleMonth data={this.state.calendarData.months[this.state.month]} month={this.state.month} />
            </div>;
        } else {
            return <div></div>;
        }
    }
}