import React from 'react';

import PeopleController from '../controller/PeopleController';
import CalendarController from '../controller/CalendarController';

export default class MainDashboard extends React.Component {


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
                    <td>{CalendarController.getNameOfMonth(birthday.birthday.month)}&nbsp;{birthday.birthday.dayOfMonth}</td>
                    <td>{birthday.name}</td>
                    <td>t.b.d.</td>
                </tr>
            );
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Main Dashboard</h1>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h4 className="card-header">Weather</h4>
                        <div className="card-block">
                            <p className="card-text">Simple weather forecast. </p>
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h4 className="card-header">Today's and next Birthdays</h4>
                        <div className="card-block">
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
                        <h4 className="card-header">Today's appointments</h4>
                        <div className="card-block">
                            <p className="card-text">...</p>
                        </div>
                    </div>
                </div>
                <div className="col-md-6">
                    <div className="card">
                        <h4 className="card-header">To be done today.</h4>
                        <div className="card-block">
                            <p className="card-text">...</p>
                        </div>
                    </div>
                </div>
            </div >
        );
    }
}
