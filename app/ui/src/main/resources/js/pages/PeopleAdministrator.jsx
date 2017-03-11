import React from 'react';
import { Link } from 'react-router';

import PeopleController from '../controller/PeopleController';

export default class PeopleAdministrator extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { people: [] };
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

    render() {
        var rows = [];
        var people = this.state.people;
        for ( var i = 0; i < people.length; i++ ) {
            var user = people[i];
            rows.push(
                <tr>
                    <td>{user.name}</td>
                    <td>{JSON.stringify( user.birthday )}</td>
                </tr>
            );
        }
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>People Administrator</h1>
                    <Link className="btn btn-primary" to="/admin/people/add" role="button">Add User...</Link>
                    <table className="table table-hover">
                        <thead className="thead-inverse">
                            <tr>
                                <th>Name</th>
                                <th>Birthday</th>
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
