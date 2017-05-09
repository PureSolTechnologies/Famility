import React from 'react';

import store from '../../flux/Store';
import { changeYear, changeMonth, changeDay } from '../../flux/CalendarActions';

import DayView from '../../components/calendar/DayView';

import CalendarController from '../../controller/CalendarController';

export default class DayCalendar extends React.Component {


    constructor( props ) {
        super( props );
        let year = parseInt( this.props.params.year );
        let month = parseInt( this.props.params.month );
        let day = parseInt( this.props.params.day );
        let storedCalendar = store.getState().calendar;
        if (storedCalendar.year !== year) {
            store.dispatch( changeYear( year ) );
        }       
        if (storedCalendar.month !== month) {
            store.dispatch( changeMonth( month ) );
        }       
        if( storedCalendar.day !== day ) {
            store.dispatch( changeDay( day ) );
        }        
        this.state = { 
            year: year, 
            month: month, 
            day: day, 
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
        const yearState = store.getState().calendar.year;
        this.readCalendar( yearState );
    }

    readCalendar( year ) {
        let component = this;
        CalendarController.getCalendar( year,
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
           <DayView calendar={this.state.calendarData} month={this.state.month} day={this.state.day} />
        </div >;
    }
}