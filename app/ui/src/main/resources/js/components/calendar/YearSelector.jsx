import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import { changeYear } from '../../flux/CalendarActions';
import store from '../../flux/Store';

export default class YearSelector extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            year: store.getState().calendar.year
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
        const yearState = store.getState().calendar.year;
        if ( this.state.year != yearState ) {
            this.setState( { year: yearState });
        }
    }

    previous() {
        store.dispatch( changeYear( this.state.year - 1 ) );
    }

    next() {
        store.dispatch( changeYear( this.state.year + 1 ) );
    }

    render() {
        return ( <span><ArrowLeftIcon onClick={this.previous} /> {this.state.year} <ArrowRightIcon onClick={this.next} /></span> );
    }

}
