import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import SingleMonth from './SingleMonth';

import MonthSelector from './MonthSelector';
import YearSelector from './YearSelector';

export default class MonthView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.string.isRequired,
        month: React.PropTypes.string.isRequired
    };

    constructor( props ) {
        super( props );
        this.state = { month: props.month, calendar: props.calendar };
    }

    componentWillReceiveProps( nextProps ) {
        if ( this.state.calendar != nextProps.calendar ) {
            this.setState( { calendar: nextProps.calendar });
        }
        if ( this.state.month != nextProps.month ) {
            this.setState( { month: nextProps.month });
        }
    }

    render() {
        return <div>
            <h1><MonthSelector />.&nbsp;<YearSelector /></h1>
            <SingleMonth month={this.state.calendar.months[this.state.month].name} data={this.state.calendar.months[this.state.month]} />
        </div>;
    }
}