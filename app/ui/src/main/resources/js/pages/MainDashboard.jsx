import React from 'react';

import LocalWeather from '../components/LocalWeather';
import PeopleController from '../controller/PeopleController';
import CalendarController from '../controller/CalendarController';
import AppointmentsToday from '../components/calendar/AppointmentsToday';
import AppointmentsTomorrow from '../components/calendar/AppointmentsTomorrow';

export default class MainDashboard extends React.Component {
    1

    constructor( props ) {
        super( props );
        this.state = { birthdays: [] };
    }

    componentDidMount() {
        var component = this;
        PeopleController.getBirthdays(
            function( birthdays ) {
                component.setState( { birthdays: birthdays });
            },
            function( response ) { }
        );
    }

    render() {
        var birthdayRows = [];
        var birthdays = this.state.birthdays;
        for ( var i = 0; i < birthdays.length; i++ ) {
            const birthday = birthdays[i];
            birthdayRows.push(
                <tr key={birthday.id}>
                    <td>{CalendarController.getNameOfMonth( birthday.nextAnniversary.month )} &nbsp;{birthday.nextAnniversary.dayOfMonth} &nbsp;{birthday.nextAnniversary.year}</td>
                    <td>{birthday.name}</td>
                    <td>{birthday.nextAge}</td>
                </tr>
            );
        }
        return (
            <div className="row">
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Weather</h3>
                        <div className="card-block" style={{padding: "0pt"}}>
                            <LocalWeather />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">Today's and next Birthdays</h3>
                        <div className="card-block" style={{padding: "0pt"}}>
                            <table className="table table-hover">
                                <thead className="thead-default">
                                    <tr>
                                        <th>Birthday</th>
                                        <th>Name</th>
                                        <th>Age</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {birthdayRows}
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                        <div className="card">
                        <h3 className="card-header">Today's appointments</h3>
                        <div className="card-block" style={{padding: "0pt"}}>
                            <AppointmentsToday />
                        </div>
                    </div>
                        <div className="card">
                        <h3 className="card-header">Tomorrow's appointments</h3>
                        <div className="card-block" style={{padding: "0pt"}}>
                            <AppointmentsTomorrow />
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h3 className="card-header">To be done today.</h3>
                        <div className="card-block">
                            <p className="card-text">...</p>
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}
