import React from 'react';
import { ArrowLeftIcon, ArrowRightIcon } from 'react-octicons';

import store from '../../flux/Store';

export default class WeekView extends React.Component {

    static propTypes = {
        calendar: React.PropTypes.object.isRequired,
        week: React.PropTypes.number.isRequired,
    };

    unsubscribeStore = null;

    constructor( props ) {
        super( props );
        this.state = { 
                week: props.week, 
                calendar: props.calendar, 
                entries: [] 
        };
        this.previousWeek = this.previousWeek.bind( this );
        this.nextWeek = this.nextWeek.bind( this );
    }

    componentDidMount() {
        this.unsubscribeStore = store.subscribe(() => this.update() );
    }

    componentWillUnmount() {
        this.unsubscribeStore();
    }

    componentWillReceiveProps( nextProps ) {
        if ( this.state.calendar != nextProps.calendar ) {
            this.setState( { calendar: nextProps.calendar });
        }
        if ( this.state.month != nextProps.month ) {
            this.setState( { month: nextProps.month });
        }
        if ( this.state.day != nextProps.day ) {
            this.setState( { day: nextProps.day });
        }
    }

    getDate( week, day ) {
        if ( !week[day] ) {
            return '';
        }
        return week[day].dayOfMonth + '.' + week[day].month + '.' + week[day].year;
    }

    previousWeek() {
        //store.dispatch( changeYear( this.state.year - 1 ) );
    }

    nextWeek() {
        // store.dispatch( changeYear( this.state.year + 1 ) );
    }


    render() {        
        if (this.state.calendar) {
            var rows = [];           
            var week = this.state.calendar.weeks[this.state.week];
            for ( var i = 0; i <= 23; i++ ) {
                rows.push(
                    <tr key={i}>
                        <th>{i} h</th>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                    </tr>
                );
            }
            return <div>          
                <table style={{bgcolor: '#ff0000'}} className="table table-hover">
                    <thead className="thead-inverse">
                        <tr>
                            <th style={{width:'9%'}}></th>
                            <th style={{width:'13%'}}>{this.getDate( week, 1 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 2 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 3 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 4 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 5 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 6 )}</th>
                            <th style={{width:'13%'}}>{this.getDate( week, 7 )}</th>
                        </tr>
                        <tr>
                            <th>Time</th>
                            <th>Monday</th>
                            <th>Tuesday</th>
                            <th>Wednesday</th>
                            <th>Thursday</th>
                            <th>Friday</th>
                            <th>Saturday</th>
                            <th>Sunday</th>
                        </tr>
                    </thead>
                    <tbody>
                            {rows}
                    </tbody>
                </table>
            </div>;
        } else {
            return <div></div>;
        }
    }
}