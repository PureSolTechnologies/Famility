import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import SingleMonth from './SingleMonth';

export default class MonthView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { month: 1, calendar: props.calendar };
        this.previousYear = this.previousYear.bind( this );
        this.nextYear = this.nextYear.bind( this );
    }

    componentWillReceiveProps( nextProps ) {
        if ( this.props.calendar != nextProps.calendar ) {
            this.setState( { calendar: nextProps.calendar });
        }
    }

    previousYear() {
        var newMonth = this.state.month - 1;
        this.setState( { month: newMonth });
    }

    nextYear() {
        var newMonth = this.state.month + 1;
        this.setState( { month: newMonth });
    }

    render() {
        return <div>
            <h1><ArrowLeftIcon onClick={this.previousYear} />{this.state.calendar.months[this.state.month].name}<ArrowRightIcon onClick={this.nextYear} /> {this.state.calendar.year}</h1>
            <SingleMonth month={this.state.calendar.months[this.state.month].name} data={this.state.calendar.months[this.state.month]} />
        </div>;
    }
}