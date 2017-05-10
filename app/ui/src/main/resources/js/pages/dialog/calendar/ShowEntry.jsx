import React from 'react';
import { browserHistory } from 'react-router';

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import Entry from '../../../models/calendar/Entry';
import CalendarDay from '../../../models/calendar/CalendarDay';
import CalendarTime from '../../../models/calendar/CalendarTime';
import TimeZoneSelector from '../../../components/calendar/TimeZoneSelector';

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
            <form className="border-0">
                <h3>Time</h3>
                <div className="row">
                    <div className="col-md-6">
                        <label htmlFor="date">Date</label>
                        <input type="date" className="form-control" id="date" value={entry.date} onChange={this.changeDate}></input>
                    </div>
                    <div className="col-md-6">
                        <TimeZoneSelector onChange={this.changeTimezone} value={entry.timezone} />
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-2">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="checkbox" checked={entry.wholeDay} onChange={this.changeWholeDay} />&nbsp;Whole Day
                            </label>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <label htmlFor="beginTime">Begin</label>
                        <input type="time" className="form-control" id="beginTime" disabled={entry.wholeDay} value={entry.beginTime} onChange={this.changeBeginTime}></input>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="duration">Duration</label>
                        <div className="input-group">
                            <input type="number" className="form-control" id="period" placeholder="" disabled={entry.wholeDay} value={entry.durationAmount} onChange={this.changeDurationAmount} />
                            <select className="form-control" disabled={entry.wholeDay} value={entry.durationUnit} onChange={this.changeDurationUnit}>
                                {durationUnits}
                            </select>
                        </div>
                    </div>
                </div>
                <hr />
                <h3>Information</h3>
                <div className="row">
                    <div className="col-md-3">
                        <label htmlFor="entryType">Entry Type</label>
                        <select className="form-control" id="entryType" value={entry.type} onChange={this.changeType}>
                            {entryTypes}
                        </select>
                    </div>
                    <div className="col-md-9">
                        <label htmlFor="title">Title</label>
                        <input type="text" className="form-control" id="title" placeholder="Title" value={entry.title} onChange={this.changeTitle}></input>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea className="form-control" id="description" rows="5" value={entry.description} onChange={this.changeDescription}></textarea>
                </div>
                <hr />
                <h3>Sharing and Reminding</h3>
                <div className="row">
                    <div className="col-md-6">
                        <label htmlFor="participans">Participans</label>
                        <textarea className="form-control" id="participans" rows="5"></textarea>
                    </div>
                    <div className="col-md-6">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="checkbox" checked={entry.reminding} onChange={this.changeReminding} />&nbsp;Reminder
                        </label>
                        </div>
                        <div className="form-group">
                            <label htmlFor="period">Period</label>
                            <div className="input-group">
                                <input type="number" className="form-control" id="period" placeholder="" disabled={!entry.reminding} value={entry.reminderDurationAmount} onChange={this.changeReminderDurationAmount} />
                                <select className="form-control" disabled={!entry.reminding} value={entry.reminderDurationUnit} onChange={this.reminderDurationUnit}>
                                    {reminderDurationUnits}
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <h3>Recurrence</h3>
                <div className="row">
                    <div className="col-md-2">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="checkbox" checked={entry.recurring} onChange={this.changeRecurring} />&nbsp;Recurrence
                            </label>
                        </div>
                    </div>
                    <div className="col-md-5">
                        <label htmlFor="turnus">Turnus</label>
                        <select className="form-control" id="turnus" disabled={!entry.recurring} value={entry.turnus} onChange={this.changeTurnus}>
                            {turnusUnits}
                        </select>
                    </div>
                    <div className="col-md-5">
                        <label htmlFor="turnusSkips">Turnus Skips</label>
                        <input type="number" className="form-control" id="turnusSkips" placeholder="" disabled={!entry.recurring} value={entry.turnusSkips} onChange={this.changeTurnusSkips} />
                    </div>
                </div>
                <hr />
                <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
            </form>
        </Dialog>;
    }
}
