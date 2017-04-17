import React from 'react';

import store from '../../flux/Store';
import { changeYear } from '../../flux/CalendarActions';

import YearSelector from '../../components/calendar/YearSelector';
import YearView from '../../components/calendar/YearView';

/**
 * This component shows a single year in calendar form.
 */
export default class YearCalendar extends React.Component {

    constructor( props ) {
        super( props );
        var year = parseInt( this.props.params.year );
        var storedCalendar = store.getState().calendar;
        if ( storedCalendar.year !== year ) {
            store.dispatch( changeYear( year ) );
        }
        this.state = { year: year };
    }

    componentDidMount() {
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        const year = store.getState().calendar.year;
        if ( year !== this.state.year ) {
            this.setState( { year: year });
        }
    }

    render() {
        return <div>
            <h1><YearSelector /></h1>
            <YearView year={this.state.year} />
        </div >;
    }
}