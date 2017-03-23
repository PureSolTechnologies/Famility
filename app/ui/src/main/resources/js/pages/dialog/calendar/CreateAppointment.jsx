import React from 'react';

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';

export default class CreatAppointment extends React.Component {


    constructor( props ) {
        super( props );
    }

    render() {
        return <Dialog title="Create Appointment">
            <h3>Information</h3>
            <form className="border-0">
                <div className="row">
                    <div className="col-lg-3">
                        <label htmlFor="appointmentType">Appointment Type</label>
                        <div className="dropdown">
                            <button className="btn btn-secondary dropdown-toggle" type="button" id="appointmentType" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                Type
                            </button>
                            <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                <button className="dropdown-item" type="button">General</button>
                                <button className="dropdown-item" type="button">Birthday</button>
                                <button className="dropdown-item" type="button">Anniversary</button>
                                <button className="dropdown-item" type="button">TODO</button>
                            </div>
                        </div>                    </div>
                    <div className="col-lg-9">
                        <div className="form-group">
                            <label htmlFor="title">Title</label>
                            <input type="text" className="form-control" id="title" placeholder="Title" />
                        </div>
                    </div>
                </div>
                <div className="form-group">
                    <label htmlFor="description">Description</label>
                    <textarea className="form-control" id="description" rows="5"></textarea>
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
                                <input className="form-check-input" type="checkbox" value="" />&nbsp;Reminder
                        </label>
                        </div>
                        <div className="form-group">
                            <label htmlFor="period">Period</label>
                            <div className="input-group">
                                <input type="text" className="form-control" id="period" placeholder="" />
                                <div className="dropdown input-group-addon">
                                    <button className="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        Time Unit
                                </button>
                                    <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                                        <button className="dropdown-item" type="button">Minutes</button>
                                        <button className="dropdown-item" type="button">Hours</button>
                                        <button className="dropdown-item" type="button">Days</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <hr />
                <h3>Recurrence</h3>
                <hr />
                <button type="button" className="btn btn-primary">Create</button>
                <button type="button" className="btn btn-secondary">Cancel</button>
            </form>
        </Dialog>;
    }
}
