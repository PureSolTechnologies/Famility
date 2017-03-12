import React from 'react';
import { Link } from 'react-router';
import { SettingsIcon, TrashcanIcon } from 'react-octicons';

import PeopleController from '../controller/PeopleController';

export default class PeopleAdministrator extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { people: [] };
        this.deleteUser = this.deleteUser.bind( this );
    }

    componentDidMount() {
        var component = this;
        PeopleController.getPeople(
            function( people ) {
                component.setState( { people: people });
            },
            function( response ) { }
        );
    }

    deleteUser( user ) {
        var component = this;
        PeopleController.deleteUser( user.id,
            function( response ) { component.componentDidMount(); },
            function( response ) { }
        );
    }
    render() {
        var rows = [];
        var people = this.state.people;
        for ( var i = 0; i < people.length; i++ ) {
            const user = people[i];
            rows.push(
                <tr key={user.id}>
                    <td>{user.name}</td>
                    <td>{user.birthday.year + "-" + user.birthday.month + "-" + user.birthday.dayOfMonth}</td>
                    <td><button className="btn btn-secondary"><SettingsIcon />&nbsp;Edit...</button>&nbsp;<button className="btn btn-secondary" onClick={() => this.deleteUser( user )} ><TrashcanIcon />&nbsp;Delete</button></td>
                </tr>
            );
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>People Administrator</h1>
                    <Link className="btn btn-primary" to="/admin/people/add" role="button">Add User...</Link>
                    <table className="table table-hover">
                        <thead className="thead-default">
                            <tr>
                                <th>Name</th>
                                <th>Birthday</th>
                                <th></th>
                            </tr>
                        </thead>
                        <tbody>
                            {rows}
                        </tbody>
                    </table>
                </div>
            </div >
        );
    }
}
