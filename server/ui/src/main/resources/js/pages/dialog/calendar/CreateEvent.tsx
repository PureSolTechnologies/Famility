import * as React from 'react';
import { browserHistory } from 'react-router';

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import CalendarEvent from '../../../models/calendar/CalendarEvent';
import CalendarDay from '../../../models/calendar/CalendarDay';
import CalendarTime from '../../../models/calendar/CalendarTime';
import TimeZoneSelector from '../../../components/calendar/TimeZoneSelector';

export default class CreateEvent extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = {
            beginDate: '',
            beginTimezone: 'Europe/Berlin',
            beginTime: '',
            endDate: '',
            endTimezone: 'Europe/Berlin',
            endTime: '',
            wholeDay: false,
            type: 'appointment',
            eventTypes: [],
            title: '',
            description: '',
            participans: [],
            reminding: false,
            reminderDurationAmount: 15,
            reminderDurationUnit: 'MINUTES',
            occupancy: 'OCCUPIED',
            reminderDurationUnits: [],
            recurring: false,
            turnus: "WEEK",
            turnusUnits: [],
            turnusSkips: 0
        };
        if ( this.props.params.date ) {
            this.state.beginDate = this.props.params.date;
        }
        if ( this.props.params.beginTime ) {
            this.state.beginTime = this.props.params.beginTime;
        }
        this.changeBeginDate = this.changeBeginDate.bind( this );
        this.changeBeginTime = this.changeBeginTime.bind( this );
        this.changeBeginTimezone = this.changeBeginTimezone.bind( this );
        this.changeEndDate = this.changeEndDate.bind( this );
        this.changeEndTime = this.changeEndTime.bind( this );
        this.changeEndTimezone = this.changeEndTimezone.bind( this );
        this.changeWholeDay = this.changeWholeDay.bind( this );
        this.changeDurationAmount = this.changeDurationAmount.bind( this );
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
        CalendarController.getEventTypes(
            function( eventTypes ) {
                component.setState( {
                    eventTypes: eventTypes
                });
            },
            function( response ) { });
    }

    changeBeginDate( event: any ): void {
        var date = event.target.value;
        this.setState( {
            beginDate: date
        });
    }

    changeBeginTime( event: any ): void {
        var time = event.target.value;
        this.setState( {
            beginTime: time
        });
    }

    changeBeginTimezone( timezone: any ): void {
        this.setState( { timezone: timezone });
    }

    changeEndDate( event: any ): void {
        var date = event.target.value;
        this.setState( {
            endDate: date
        });
    }

    changeEndTime( event: any ): void {
        var time = event.target.value;
        this.setState( {
            endTime: time
        });
    }

    changeEndTimezone( timezone: any ): void {
        this.setState( { timezone: timezone });
    }

    changeWholeDay( event: any ): void {
        var wholeDay = event.target.checked;
        this.setState( {
            wholeDay: wholeDay
        });
    }

    changeDurationAmount( event: any ): void {
        var durationAmount = event.target.value;
        this.setState( {
            durationAmount: durationAmount
        });
    }

    changeType( event: any ): void {
        var type = event.target.value;
        this.setState( {
            type: type
        });
    }

    changeTitle( event: any ): void {
        var title = event.target.value;
        this.setState( {
            title: title
        });
    }

    changeDescription( event: any ): void {
        var description = event.target.value;
        this.setState( {
            description: description
        });
    }

    changeReminding( event: any ): void {
        var reminding = event.target.checked;
        this.setState( {
            reminding: reminding
        });
    }

    changeReminderDurationAmount( event: any ): void {
        var amount = event.target.value;
        this.setState( {
            reminderDurationAmount: amount
        });
    }

    changeReminderDurationUnit( event: any ): void {
        var unit = event.target.value;
        this.setState( {
            reminderDurationUnit: unit
        });
    }

    changeRecurring( event: any ): void {
        var recurring = event.target.checked;
        this.setState( {
            recurring: recurring
        });
    }

    changeTurnus( event: any ): void {
        var turnus = event.target.value;
        this.setState( {
            turnus: turnus
        });
    }

    changeTurnusSkips( event: any ): void {
        var turnusSkips = event.target.value;
        this.setState( {
            turnusSkips: turnusSkips
        });
    }

    create() {
        var component = this;
        var event = new CalendarEvent();
        event.id = -1;
        event.type = this.state.type;
        event.title = this.state.title;
        event.description = this.state.description;
        event.participants = [];
        event.reminding = this.state.reminding;
        event.reminder = {
            amount: this.state.reminderDurationAmount,
            unit: this.state.reminderDurationUnit
        };
        event.beginDate = CalendarDay.fromString( this.state.beginDate );
        event.beginTimezone = this.state.beginTimezone;
        event.beginTime = CalendarTime.fromString( this.state.beginTime );
        event.endDate = CalendarDay.fromString( this.state.endDate );
        event.endTimezone = this.state.endTimezone;
        event.endTime = CalendarTime.fromString( this.state.endTime );
        event.occupancy = this.state.occupancy;

        CalendarController.createEvent( event,
            function( response ) {
                component.props.router.push( '/calendar/day/' + event.beginDate.year + '/' + event.beginDate.month + '/' + event.beginDate.dayOfMonth );
            },
            function( response ) { });
    }

    render() {
        var eventTypes: any[] = [];
        for ( var eventType of this.state.eventTypes ) {
            eventTypes.push( <option key={eventType.type} value={eventType.type}>{eventType.name}</option> );
        }
        var reminderDurationUnits: any[] = [];
        for ( var reminderDurationUnit of this.state.reminderDurationUnits ) {
            reminderDurationUnits.push( <option key={reminderDurationUnit.unit} value={reminderDurationUnit.unit}>{reminderDurationUnit.name}</option> );
        }
        var turnusUnits: any[] = [];
        for ( var turnusUnit of this.state.turnusUnits ) {
            turnusUnits.push( <option key={turnusUnit.unit} value={turnusUnit.unit}>{turnusUnit.name}</option> );
        }
        return <Dialog title="Create Calendar Event">
            <form className="border-0">
                <h3>Time</h3>
                <div className="row">
                <div className="col-md-4">
                    <label htmlFor="beginDate">Date</label>
                    <input type="date" className="form-control" id="beginDate" value={this.state.beginDate} onChange={this.changeBeginDate}></input>
                </div>
                <div className="col-md-4">
                    <label htmlFor="beginTime">Begin</label>
                    <input type="time" className="form-control" id="beginTime" disabled={this.state.wholeDay} value={this.state.beginTime} onChange={this.changeBeginTime}></input>
                </div>
                <div className="col-md-4">
                    <TimeZoneSelector onChange={this.changeBeginTimezone} value={this.state.beginTimezone} />
                </div>
            </div>
                    <div className="row">
                    <div className="col-md-4">
                        <label htmlFor="endDate">Date</label>
                        <input type="date" className="form-control" id="endDate" value={this.state.endDate} onChange={this.changeEndDate}></input>
                    </div>
                    <div className="col-md-4">
                        <label htmlFor="endTime">Begin</label>
                        <input type="time" className="form-control" id="endTime" disabled={this.state.wholeDay} value={this.state.endTime} onChange={this.changeEndTime}></input>
                    </div>
                    <div className="col-md-4">
                        <TimeZoneSelector onChange={this.changeEndTimezone} value={this.state.endTimezone} />
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
                    <div className="col-md-6">
                    </div>
                </div>
                <hr />
                <h3>Information</h3>
                <div className="row">
                    <div className="col-md-3">
                        <label htmlFor="eventType">Event Type</label>
                        <select className="form-control" id="eventType" value={this.state.type} onChange={this.changeType}>
                            {eventTypes}
                        </select>
                    </div>
                    <div className="col-md-9">
                        <label htmlFor="title">Title</label>
                        <input type="text" className="form-control" id="title" placeholder="Title" value={this.state.title} onChange={this.changeTitle}></input>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea className="form-control" id="description" rows={5} value={this.state.description} onChange={this.changeDescription}></textarea>
                </div>
                <hr />
                <h3>Sharing and Reminding</h3>
                <div className="row">
                    <div className="col-md-6">
                        <label htmlFor="participans">Participans</label>
                        <textarea className="form-control" id="participans" rows={5}></textarea>
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
                                <select className="form-control" disabled={!this.state.reminding} value={this.state.reminderDurationUnit} onChange={this.state.reminderDurationUnit}>
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
