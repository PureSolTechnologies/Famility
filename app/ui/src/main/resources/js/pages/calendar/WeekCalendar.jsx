import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../../flux/Store';
import { changeYear, changeMonth, changeDay } from '../../flux/CalendarActions';

import WeekView from '../../components/calendar/WeekView';

import CalendarController from '../../controller/CalendarController';

export default class WeekCalendar extends React.Component {

    constructor( props ) {
        super( props );
        let year = parseInt( this.props.params.year );
        let week = parseInt( this.props.params.week );
        let storedCalendar = store.getState().calendar;
        if ( storedCalendar !== year ) {
            store.dispatch( changeYear( year ) );
        }
        this.state = {
            year: year,
            week: week,
            calendarData: null
        };
    }

    componentDidMount() {
        this.readCalendar( this.state.year );
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        const year = store.getState().calendar.year;
        this.readCalendar( year );
    }

    readCalendar( year ) {
        let component = this;
        CalendarController.getCalendar( year,
            function( calendar ) {
                let week = component.state.week;
                component.setState( { year: year, week: week, calendarData: calendar });
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
            <WeekView calendar={this.state.calendarData} week={this.state.week} />
        </div >;
    }
}