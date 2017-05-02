import * as  React from 'react';

import CalendarController from '../../controller/CalendarController';
import PeopleController from '../../controller/PeopleController';

export default class Birthdays extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = { birthdays: [] };
    }

    componentDidMount() {
        var component = this;
        PeopleController.getBirthdays(
            function( birthdays: any ) {
                component.setState( { birthdays: birthdays });
            },
            function( response: any ) { }
        );
    }

    render() {
        var birthdayRows: any = [];
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
        );
    }

}