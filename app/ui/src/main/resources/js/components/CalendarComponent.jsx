import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../flux/Store';

import Tab from './Tab';
import TabComponent from './TabComponent';

import DayView from './calendar/DayView';
import WeekView from './calendar/WeekView';
import MonthView from './calendar/MonthView';
import YearView from './calendar/YearView';

import YearSelector from './calendar/YearSelector';
import MonthSelector from './calendar/MonthSelector';
import DaySelector from './calendar/DaySelector';

import CalendarController from '../controller/CalendarController';

export default class CalendarComponent extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { calendar: store.getState().calendar, calendarData: null };
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
        CalendarController.getYear( year,
            function( calendar ) {
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
            <h1><DaySelector />.&nbsp;<MonthSelector />.&nbsp;<YearSelector /></h1>
            <TabComponent>
                <Tab heading="Year"><YearView calendar={this.state.calendarData} /></Tab>
                <Tab heading="Month"><MonthView calendar={this.state.calendarData} month={this.state.calendar.month} /></Tab>
                <Tab heading="Week"><WeekView calendar={this.state.calendarData} month={this.state.calendar.month} day={this.state.calendar.day} /></Tab>
                <Tab heading="Day"><DayView calendar={this.state.calendarData} month={this.state.calendar.month} day={this.state.calendar.day} /></Tab>
            </TabComponent>
        </div>;
    }
}