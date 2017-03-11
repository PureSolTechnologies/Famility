import React from 'react';

import PeopleController from '../controller/PeopleController';

export default class AddUser extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { userName: '', birthday: '' };
        this.handleNameChange = this.handleNameChange.bind( this );
        this.handleBirthdayChange = this.handleBirthdayChange.bind( this );
        this.addUser = this.addUser.bind( this );
    }

    handleNameChange( event ) {
        this.setState( {
            userName: event.target.value
        });
    }

    handleBirthdayChange( event ) {
        this.setState( {
            birthday: event.target.value
        });
    }

    addUser() {
        PeopleController.addUser( this.state.userName, this.state.birthday );
        this.props.router.push( '/admin/people' );
    }

    render() {
        return (
            <div className="row">
                <div className="col-md-12">
                    <h1>Add User</h1>
                    <form>
                        <div className="form-group">
                            <label htmlFor="UserName">User name</label>
                            <input type="text" className="form-control" id="UserName" placeholder="Enter display name" value={this.state.userName} onChange={this.handleNameChange} />
                        </div>
                        <div className="form-group">
                            <label htmlFor="Birthday">Birthday</label>
                            <input type="date" className="form-control" id="Birthday" placeholder="Enter birthday" value={this.state.birthday} onChange={this.handleBirthdayChange} />
                        </div>
                        <button type="button" className="btn btn-primary" onClick={this.addUser}>Create...</button>
                    </form>
                </div>
            </div >
        );
    }
}


