import React from 'react';
import { browserHistory } from 'react-router';

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import Entry from '../../../models/calendar/Entry';
import CalendarDay from '../../../models/calendar/CalendarDay';
import CalendarTime from '../../../models/calendar/CalendarTime';
import TimeZoneSelector from '../../../components/calendar/TimeZoneSelector';

export default class CreateEntry extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            date: '',
            timezone: 'Europe/Berlin',
            beginTime: '',
            wholeDay: false,
            durationAmount: 1,
            durationUnit: 'HOURS',
            type: '',
            entryTypes: [],
            title: '',
            description: '',
            participans: [],
            reminding: false,
            reminderDurationAmount: 15,
            reminderDurationUnit: 'MINUTES',
            occupancy: 'OCCUPIED',
            durationUnits: [],
            reminderDurationUnits: [],
            recurring: false,
            turnus: "WEEK",
            turnusUnits: [],
            turnusSkips: 0
        };
        if ( this.props.params.date ) {
            this.state.date = this.props.params.date;
        }
        if ( this.props.params.beginTime ) {
            this.state.beginTime = this.props.params.beginTime;
        }
        this.changeDate = this.changeDate.bind( this );
        this.changeTimezone = this.changeTimezone.bind( this );
        this.changeWholeDay = this.changeWholeDay.bind( this );
        this.changeBeginTime = this.changeBeginTime.bind( this );
        this.changeDurationAmount = this.changeDurationAmount.bind( this );
        this.changeDurationUnit = this.changeDurationUnit.bind( this );
        this.changeType = this.changeType.bind( this );
        this.changeTitle = this.changeTitle.bind( this );
        this.changeDescription = this.changeDescription.bind( this );
        this.changeReminding = this.changeReminding.bind( this );
        this.changeReminderDurationAmount = this.changeReminderDurationAmount.bind( this );
        this.changeReminderDurationUnit = this.changeReminderDurationUnit.bind( this );
        this.changeRecurring = this.changeRecurring.bind( this );
        this.changeTurnus = this.changeTurnus.bind( this );
        this.changeTurnusSkips = this.changeTurnusSkips.bind( this );
        this.create = this.create.bind( this );
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
    }

    changeDate( event ) {
        var date = event.target.value;
        this.setState( {
            date: date
        });
    }

    changeTimezone( timezone ) {
        this.setState( { timezone: timezone });
    }

    changeWholeDay( event ) {
        var wholeDay = event.target.checked;
        this.setState( {
            wholeDay: wholeDay
        });
    }

    changeBeginTime( event ) {
        var beginTime = event.target.value;
        this.setState( {
            beginTime: beginTime
        });
    }

    changeDurationAmount( event ) {
        var durationAmount = event.target.value;
        this.setState( {
            durationAmount: durationAmount
        });
    }

    changeDurationUnit( event ) {
        var durationUnit = event.target.value;
        this.setState( {
            durationUnit: durationUnit
        });
    }

    changeType( event ) {
        var type = event.target.value;
        this.setState( {
            type: type
        });
    }

    changeTitle( event ) {
        var title = event.target.value;
        this.setState( {
            title: title
        });
    }

    changeDescription( event ) {
        var description = event.target.value;
        this.setState( {
            description: description
        });
    }

    changeReminding( event ) {
        var reminding = event.target.checked;
        this.setState( {
            reminding: reminding
        });
    }

    changeReminderDurationAmount( event ) {
        var amount = event.target.value;
        this.setState( {
            reminderDurationAmount: amount
        });
    }

    changeReminderDurationUnit( event ) {
        var unit = event.target.value;
        this.setState( {
            reminderDurationUnit: unit
        });
    }

    changeRecurring(event) {
        var recurring = event.target.checked;
        this.setState( {
            recurring: recurring
        });        
    }
    
    changeTurnus(event) {
        var turnus = event.target.value;
        this.setState( {
            turnus: turnus
        });                
    }
    
    changeTurnusSkips(event) {
        var turnusSkips = event.target.value;
        this.setState( {
            turnusSkips: turnusSkips
        });                        
    }

    create() {
        var component = this;
        var entry = new Entry;
        entry.id = -1;
        entry.type = this.state.type;
        entry.title = this.state.title;
        entry.description = this.state.description;
        entry.participants = [];
        entry.reminding = this.state.reminding;
        entry.reminder = {
            amount: this.state.reminderDurationAmount,
            unit: this.state.reminderDurationUnit
        };
        entry.date = CalendarDay.fromString( this.state.date );
        entry.timezone = this.state.timezone;
        entry.time = CalendarTime.fromString( this.state.beginTime );
        entry.durationAmount = this.state.durationAmount;
        entry.durationUnit = this.state.durationUnit;
        entry.occupancy = this.state.occupancy;

        CalendarController.createEntry( entry,
            function( response ) {
                component.props.router.push( '/calendar/day/' + entry.date.year + '/' + entry.date.month + '/' + entry.date.dayOfMonth );
            },
            function( response ) { });
    }

    render() {
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
        return <Dialog title="Create Calendar Entry">
            <form className="border-0">
                <h3>Time</h3>
                <div className="row">
                    <div className="col-md-6">
                        <label htmlFor="date">Date</label>
                        <input type="date" className="form-control" id="date" value={this.state.date} onChange={this.changeDate}></input>
                    </div>
                    <div className="col-md-6">
                        <TimeZoneSelector onChange={this.changeTimezone} value={this.state.timezone} />
                    </div>
                </div>
                <div className="row">
                    <div className="col-md-2">
                        <div className="form-check">
                            <label className="form-check-label">
                                <input className="form-check-input" type="checkbox" checked={this.state.wholeDay} onChange={this.changeWholeDay} />&nbsp;Whole Day
                            </label>
                        </div>
                    </div>
                    <div className="col-md-4">
                        <label htmlFor="beginTime">Begin</label>
                        <input type="time" className="form-control" id="beginTime" disabled={this.state.wholeDay} value={this.state.beginTime} onChange={this.changeBeginTime}></input>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="duration">Duration</label>
                        <div className="input-group">
                            <input type="number" className="form-control" id="period" placeholder="" disabled={this.state.wholeDay} value={this.state.durationAmount} onChange={this.changeDurationAmount} />
                            <select className="form-control" disabled={this.state.wholeDay} value={this.state.durationUnit} onChange={this.changeDurationUnit}>
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
                        <select className="form-control" id="entryType" value={this.state.type} onChange={this.changeType}>
                            {entryTypes}
                        </select>
                    </div>
                    <div className="col-md-9">
                        <label htmlFor="title">Title</label>
                        <input type="text" className="form-control" id="title" placeholder="Title" value={this.state.title} onChange={this.changeTitle}></input>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea className="form-control" id="description" rows="5" value={this.state.description} onChange={this.changeDescription}></textarea>
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
                                <input className="form-check-input" type="checkbox" checked={this.state.reminding} onChange={this.changeReminding} />&nbsp;Reminder
                        </label>
                        </div>
                        <div className="form-group">
                            <label htmlFor="period">Period</label>
                            <div className="input-group">
                                <input type="number" className="form-control" id="period" placeholder="" disabled={!this.state.reminding} value={this.state.reminderDurationAmount} onChange={this.changeReminderDurationAmount} />
                                <select className="form-control" disabled={!this.state.reminding} value={this.state.reminderDurationUnit} onChange={this.reminderDurationUnit}>
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
                                <input className="form-check-input" type="checkbox" checked={this.state.recurring} onChange={this.changeRecurring} />&nbsp;Recurrence
                            </label>
                        </div>
                    </div>
                    <div className="col-md-5">
                        <label htmlFor="turnus">Turnus</label>
                        <select className="form-control" id="turnus" disabled={!this.state.recurring} value={this.state.turnus} onChange={this.changeTurnus}>
                            {turnusUnits}
                        </select>
                    </div>
                    <div className="col-md-5">
                        <label htmlFor="turnusSkips">Turnus Skips</label>
                        <input type="number" className="form-control" id="turnusSkips" placeholder="" disabled={!this.state.recurring} value={this.state.turnusSkips} onChange={this.changeTurnusSkips} />
                    </div>
                </div>
                <hr />
                <button type="button" className="btn btn-primary" onClick={this.create}>Create</button>
                <button type="button" className="btn btn-secondary" onClick={browserHistory.goBack}>Cancel</button>
            </form>
        </Dialog>;
    }
}
