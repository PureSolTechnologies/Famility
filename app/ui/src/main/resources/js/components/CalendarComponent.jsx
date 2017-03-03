import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import Tab from './Tab';
import TabComponent from './TabComponent';

import DayView from './calendar/DayView';
import WeekView from './calendar/WeekView';
import MonthView from './calendar/MonthView';
import YearView from './calendar/YearView';

import CalendarController from '../controller/CalendarController';

export default class CalendarComponent extends React.Component {

    static propTypes = {
        year: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { year: props.year, calendar: null };
        this.previousYear = this.previousYear.bind( this );
        this.nextYear = this.nextYear.bind( this );
    }

    componentDidMount() {
        this.readCalendar( this.state.year );
    }

    readCalendar( year ) {
        var component = this;
        CalendarController.getYear( year,
            function( response ) {
                component.setState( { year: year, calendar: JSON.parse( response.response ) });
            },
            function( response ) {
            }
        );
    }

    previousYear() {
        var newYear = parseInt( this.state.year ) - 1;
        this.readCalendar( newYear );
    }

    nextYear() {
        var newYear = parseInt( this.state.year ) + 1;
        this.readCalendar( newYear );
    }

    render() {
        if ( !this.state.calendar ) {
            return <div></div>;
        }
        return <div>
            <h1><ArrowLeftIcon onClick={this.previousYear} />{this.state.year}<ArrowRightIcon onClick={this.nextYear} /></h1>
            <TabComponent>
                <Tab heading="Year"><YearView calendar={this.state.calendar} /></Tab>
                <Tab heading="Month"><MonthView calendar={this.state.calendar} /></Tab>
                <Tab heading="Week"><WeekView calendar={this.state.calendar} /></Tab>
                <Tab heading="Day"><DayView calendar={this.state.calendar} /></Tab>
            </TabComponent>
        </div>;
    }
}