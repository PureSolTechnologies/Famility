import React from 'react';

import store from '../../flux/Store';
import { changeYear, changeMonth } from '../../flux/CalendarActions';

import MonthSelector from '../../components/calendar/MonthSelector';
import YearSelector from '../../components/calendar/YearSelector';
import MonthView from '../../components/calendar/MonthView';

export default class MonthCalendar extends React.Component {

    constructor( props ) {
        super( props );
        var year = parseInt( this.props.params.year );
        var month = parseInt( this.props.params.month );
        var storedCalendar = store.getState().calendar;
        if ( storedCalendar.year !== year ) {
            store.dispatch( changeYear( year ) );
        }
        if ( storedCalendar.month !== month ) {
            store.dispatch( changeMonth( month ) );
        }
        this.state = { year: year, month: month };
    }

    componentDidMount() {
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        var calendar = store.getState().calendar;
        if (( calendar.year !== this.state.year ) || ( calendar.month !== this.state.month )) {
            this.setState( { year: calendar.year, month: calendar.month });
        }
    }

    render() {
        return <div>
            <h1><MonthSelector />.&nbsp;<YearSelector /></h1>
            <MonthView year={this.state.year} month={this.state.month} />
        </div >;
    }
}