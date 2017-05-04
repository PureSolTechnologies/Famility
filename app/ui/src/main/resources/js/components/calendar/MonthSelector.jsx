import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import CalendarController from '../../controller/CalendarController';
import { changeMonth } from '../../flux/CalendarActions';
import store from '../../flux/Store';

export default class MonthSelector extends React.Component {

    static propTypes = {
        name: React.PropTypes.bool
    };

    constructor( props ) {
        super( props );
        this.state = {
            month: store.getState().calendar.month
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
        const monthState = store.getState().calendar.month;
        if ( this.state.month != monthState ) {
            this.setState( { month: monthState });
        }
    }

    previous() {
        store.dispatch( changeMonth( this.state.month - 1 ) );
    }

    next() {
        store.dispatch( changeMonth( this.state.month + 1 ) );
    }

    render() {
        return ( <span><ArrowLeftIcon onClick={this.previous} />&nbsp;{this.props.name ? CalendarController.getNameOfMonth( this.state.month ) : this.state.month}<ArrowRightIcon onClick={this.next} /></span> );
    }

}
