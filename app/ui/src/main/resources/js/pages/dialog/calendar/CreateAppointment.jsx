import React from 'react';
import { browserHistory } from 'react-router';

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import Appointment from '../../../models/calendar/Appointment';
import CalendarDay from '../../../models/calendar/CalendarDay';
import CalendarTime from '../../../models/calendar/CalendarTime';
import TimeZoneSelector from '../../../components/calendar/TimeZoneSelector';

export default class CreateAppointment extends React.Component {

    constructor( props ) {
        super( props );
        this.state = {
            date: '',
            timezone: 'Europe/Berlin',
            beginTime: '',
            durationAmount: 1,
            durationUnit: 'HOURS',
            appointmentType: 'GENERAL',
            title: '',
            description: '',
            participans: [],
            reminding: false,
            reminderDurationAmount: 15,
            reminderDurationUnit: 'MINUTES',
            occupancy: 'OCCUPIED',
            durationUnits: [],
            reminderDurationUnits: []
        };
        if ( this.props.params.date ) {
            this.state.date = this.props.params.date;
        }
        if ( this.props.params.beginTime ) {
            this.state.beginTime = this.props.params.beginTime;
        }
        this.changeDate = this.changeDate.bind( this );
        this.changeTimezone = this.changeTimezone.bind( this );
        this.changeBeginTime = this.changeBeginTime.bind( this );
        this.changeDurationAmount = this.changeDurationAmount.bind( this );
        this.changeDurationUnit = this.changeDurationUnit.bind( this );
        this.changeType = this.changeType.bind( this );
        this.changeTitle = this.changeTitle.bind( this );
        this.changeDescription = this.changeDescription.bind( this );
        this.changeReminding = this.changeReminding.bind( this );
        this.changeReminderDurationAmount = this.changeReminderDurationAmount.bind( this );
        this.changeReminderDurationUnit = this.changeReminderDurationUnit.bind( this );
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
        var appointmentType = event.target.value;
        this.setState( {
            appointmentType: appointmentType
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

    create() {
        var component = this;
        var appointment = new Appointment;
        appointment.id = -1;
        appointment.type = this.state.appointmentType;
        appointment.title = this.state.title;
        appointment.description = this.state.description;
        appointment.participants = [];
        appointment.reminding = this.state.reminding;
        appointment.reminder = {
            amount: this.state.reminderDurationAmount,
            unit: this.state.reminderDurationUnit
        };
        appointment.date = CalendarDay.fromString( this.state.date );
        appointment.timezone = this.state.timezone;
        appointment.time = CalendarTime.fromString( this.state.beginTime );
        appointment.durationAmount = this.state.durationAmount;
        appointment.durationUnit = this.state.durationUnit;
        appointment.occupancy = this.state.occupancy;

        CalendarController.createAppointment( appointment,
            function( response ) {
                component.props.router.push( '/calendar/day/' + appointment.date.year + '/' + appointment.date.month + '/' + appointment.date.dayOfMonth );
            },
            function( response ) { });
    }

    render() {
        var durationUnits = [];
        for ( var durationUnit of this.state.durationUnits ) {
            durationUnits.push( <option key={durationUnit.unit} value={durationUnit.unit}>{durationUnit.name}</option> );
        }
        var reminderDurationUnits = [];
        for ( var reminderDurationUnit of this.state.reminderDurationUnits ) {
            reminderDurationUnits.push( <option key={reminderDurationUnit.unit} key={reminderDurationUnit.value}>{reminderDurationUnit.name}</option> );
        }
        return <Dialog title="Create Appointment">
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
                    <div className="col-md-6">
                        <label htmlFor="beginTime">Begin</label>
                        <input type="time" className="form-control" id="beginTime" value={this.state.beginTime} onChange={this.changeBeginTime}></input>
                    </div>
                    <div className="col-md-6">
                        <label htmlFor="duration">Duration</label>
                        <div className="input-group">
                            <input type="number" className="form-control" id="period" placeholder="" value={this.state.durationAmount} onChange={this.changeDurationAmount} />
                            <select className="form-control" value={this.state.durationUnit} onChange={this.changeDurationUnit}>
                                {durationUnits}
                            </select>
                        </div>
                    </div>
                </div>
                <hr />
                <h3>Information</h3>
                <div className="row">
                    <div className="col-lg-3">
                        <label htmlFor="appointmentType">Appointment Type</label>
                        <select className="form-control" id="appointmentType" value={this.state.appointmentType} onChange={this.changeType}>
                            <option value="GENERAL">General</option>
                            <option value="BIRTHDAY">Birthday</option>
                            <option value="ANNIVERSARY">Anniversary</option>
                            <option value="TODO">TODO</option>
                        </select>
                    </div>
                    <div className="col-lg-9">
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
                    <div className="col-lg-6">
                        <label htmlFor="participans">Participans</label>
                        <textarea className="form-control" id="participans" rows="5"></textarea>
                    </div>
                    <div className="col-lg-6">
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
                <hr />
                <button type="button" className="btn btn-primary" onClick={this.create}>Create</button>
                <button type="button" className="btn btn-secondary" onClick={browserHistory.goBack}>Cancel</button>
            </form>
        </Dialog>;
    }
}
