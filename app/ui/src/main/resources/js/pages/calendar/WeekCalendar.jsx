import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../../flux/Store';
import { changeYear, changeMonth, changeDay } from '../../flux/CalendarActions';

import Tab from '../../components/Tab';
import TabComponent from '../../components/TabComponent';

import DayView from '../../components/calendar/DayView';
import WeekView from '../../components/calendar/WeekView';
import MonthView from '../../components/calendar/MonthView';
import YearView from '../../components/calendar/YearView';

import CalendarController from '../../controller/CalendarController';

export default class WeekCalendar extends React.Component {


    constructor( props ) {
        super( props );
        var storedCalendar = store.getState().calendar;
        if ( storedCalendar !== this.props.params.year ) {
            store.dispatch( changeYear( this.props.params.year ) );
        }
        this.state = { calendar: storedCalendar, calendarData: null };
    }

    componentDidMount() {
        this.readCalendar( this.state.calendar.year );
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        const yearState = store.getState().calendar.year;
        this.readCalendar( yearState );
    }

    readCalendar( year ) {
        var component = this;
        CalendarController.getCalendar( year,
            function( calendar ) {
                var week = calendar.weeks[component.props.params.week];
                if ( week ) {
                    store.dispatch( changeMonth( week[1].month ) );
                    store.dispatch( changeDay( week[1].dayOfMonth ) );
                }
                component.setState( { calendarData: calendar });
            },
            function( response ) {
            }
        );
    }

    render() {
        if ( !this.state.calendarData ) {
            return <div></div>;
        }

        return <div>
            <WeekView calendar={this.state.calendarData} month={this.state.calendar.month} day={this.state.calendar.day} />
        </div >;
    }
}