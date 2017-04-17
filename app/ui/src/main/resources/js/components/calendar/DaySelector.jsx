import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import { changeDay } from '../../flux/CalendarActions';
import store from '../../flux/Store';

export default class DaySelector extends React.Component {

    unsubscribeStore = null;

    constructor( props ) {
        super( props );
        this.state = {
            day: store.getState().calendar.day
        }
        this.previous = this.previous.bind( this );
        this.next = this.next.bind( this );
    }

    componentDidMount() {
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    update() {
        const dayState = store.getState().calendar.day;
        if ( this.state.day != dayState ) {
            this.setState( { day: dayState });
        }
    }

    previous() {
        store.dispatch( changeDay( this.state.day - 1 ) );
    }

    next() {
        store.dispatch( changeDay( this.state.day + 1 ) );
    }

    render() {
        return ( <span><ArrowLeftIcon onClick={this.previous} /> <ArrowRightIcon onClick={this.next} /> {this.state.day}</span> );
    }

}
