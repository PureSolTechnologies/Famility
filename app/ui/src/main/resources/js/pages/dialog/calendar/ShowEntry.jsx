import React from 'react';
import { browserHistory } from 'react-router';

const { TrashcanIcon, GearIcon  } = require( 'react-octicons' );

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import Entry from '../../../models/calendar/Entry';
import CalendarDay from '../../../models/calendar/CalendarDay';
import CalendarTime from '../../../models/calendar/CalendarTime';
import TimeZoneSelector from '../../../components/calendar/TimeZoneSelector';
import EntryIcon from '../../../components/calendar/EntryIcon';

export default class ShowEntry extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            entry: {},
            entryTypes: [],
            reminderDurationUnits: [],
            durationUnits: [],
            turnusUnits: [],
        };
        this.close = this.close.bind( this );
    }

    componentDidMount() {
        var component = this;
        CalendarController.getDurationUnits(
            function( units ) {
                component.setState( {
                    durationUnits: units,
                    durationAmount: 1,
                    durationUnit: 'HOURS'
                });
            },
            function( response ) { });
        CalendarController.getReminderDurationUnits(
            function( units ) {
                component.setState( {
                    reminderDurationUnits: units,
                    reminderDurationAmount: 15,
                    reminderDurationUnit: 'MINUTES'
                });
            },
            function( response ) { });
        CalendarController.getTurnusUnits(
            function( units ) {
                component.setState( {
                    turnusUnits: units
                });
            },
            function( response ) { });
        CalendarController.getEntryTypes(
            function( entryTypes ) {
                component.setState( {
                    entryTypes: entryTypes
                });
            },
            function( response ) { });
        CalendarController.getEntry(
            this.props.params.id,
            function( entry ) {
                component.setState( {
                    entry: entry
                });
            },
            function( response ) { });
    }

    close() {
        var entry = this.state.entry;
        var referTo = this.props.location.query.origin;
        if ( referTo ) {
            this.props.router.push( referTo );
        } else {
            this.props.router.push( '/calendar/day/' + entry.date.year + '/' + entry.date.month + '/' + entry.date.dayOfMonth );
        }
    }

    render() {
        var entry = this.state.entry;
        if ( !entry.title ) {
            return <div />;
        }
        var entryTypes = [];
        for ( var entryType of this.state.entryTypes ) {
            entryTypes.push( <option key={entryType.type} value={entryType.type}>{entryType.name}</option> );
        }
        var durationUnits = [];
        for ( var durationUnit of this.state.durationUnits ) {
            durationUnits.push( <option key={durationUnit.unit} value={durationUnit.unit}>{durationUnit.name}</option> );
        }
        var reminderDurationUnits = [];
        for ( var reminderDurationUnit of this.state.reminderDurationUnits ) {
            reminderDurationUnits.push( <option key={reminderDurationUnit.unit} value={reminderDurationUnit.unit}>{reminderDurationUnit.name}</option> );
        }
        var turnusUnits = [];
        for ( var turnusUnit of this.state.turnusUnits ) {
            turnusUnits.push( <option key={turnusUnit.unit} value={turnusUnit.unit}>{turnusUnit.name}</option> );
        }
        return <Dialog title="Show Calendar Entry">
            <h2> <EntryIcon type={entry.type} /> {entry.title}  <button type="button" className="btn btn-default"><TrashcanIcon /></button><button type="button" className="btn btn-default"><GearIcon /></button></h2>
            <p>
                <i>{CalendarDay.fromEntry( entry ).toString()} {CalendarTime.fromEntry( entry ).toString()} ({entry.timezone} )</i><br />
                <i>for {entry.durationAmount} {entry.durationUnit}</i>
            </p>
            <p>
                {entry.description}
            </p>
            <p>
                {JSON.stringify(entry.participants)}
            </p>
            <hr />
            <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
        </Dialog>;
    }
}
