import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../flux/Store';

import Tab from '../components/Tab';
import TabComponent from '../components/TabComponent';

import DayView from '../components/calendar/DayView';
import WeekView from '../components/calendar/WeekView';
import MonthView from '../components/calendar/MonthView';
import YearView from '../components/calendar/YearView';

import YearSelector from '../components/calendar/YearSelector';
import MonthSelector from '../components/calendar/MonthSelector';
import DaySelector from '../components/calendar/DaySelector';

import CalendarController from '../controller/CalendarController';

export default class Calendar extends React.Component {


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
            <TabComponent selected={this.props.params.view}>
                <Tab name="year" heading="Year"><YearView calendar={this.state.calendarData} /></Tab>
                <Tab name="month" heading="Month"><MonthView calendar={this.state.calendarData} month={this.state.calendar.month} /></Tab>
                <Tab name="week" heading="Week"><WeekView calendar={this.state.calendarData} month={this.state.calendar.month} day={this.state.calendar.day} /></Tab>
                <Tab name="day" heading="Day"><DayView calendar={this.state.calendarData} month={this.state.calendar.month} day={this.state.calendar.day} /></Tab>
            </TabComponent>
        </div >;
    }
}