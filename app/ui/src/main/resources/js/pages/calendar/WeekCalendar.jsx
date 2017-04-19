import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../../flux/Store';
import { changeYear, changeMonth, changeDay } from '../../flux/CalendarActions';

import WeekView from '../../components/calendar/WeekView';

import CalendarController from '../../controller/CalendarController';

export default class WeekCalendar extends React.Component {

    constructor( props ) {
        super( props );
        var year = parseInt( this.props.params.year );
        var week = parseInt( this.props.params.week );
        this.state = { year: year, week: week, calendarData: null };
        var storedCalendar = store.getState().calendar;
        if ( storedCalendar !== year ) {
            store.dispatch( changeYear( year ) );
        }
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
        var component = this;
        CalendarController.getCalendar( year,
            function( calendar ) {
                var weekId = component.state.week;
                var week = calendar.weeks[weekId];
                if ( week ) {
                    store.dispatch( changeMonth( week[1].month ) );
                    store.dispatch( changeDay( week[1].dayOfMonth ) );
                }
                component.setState( { year: year, calendarData: calendar });
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