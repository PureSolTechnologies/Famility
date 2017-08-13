import * as React from 'react';
import { browserHistory } from 'react-router';

const { TrashcanIcon, GearIcon } = require( 'react-octicons' );

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import Loading from '../../../components/Loading';
import EventView from '../../../components/calendar/EventView';

export default class ShowEvent extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = {
            event: {}
        };
        this.close = this.close.bind( this );
    }

    componentDidMount(): void {
        var component = this;
        CalendarController.getEvent(
            this.props.params.id,
            function( event ) {
                component.setState( {
                    event: event
                } );
            },
            function( response ) { } );
    }

    edit(): void {

    }

    remove(): void {

    }

    close() {
        var event = this.state.event;
        var referTo = this.props.location.query.origin;
        if ( referTo ) {
            this.props.router.push( referTo );
        } else {
            this.props.router.push( '/calendar/day/' + event.date.year + '/' + event.date.month + '/' + event.date.dayOfMonth );
        }
    }

    render() {
        var event = this.state.event;
        if ( !event.title ) {
            return (
                <Dialog title="Show Calendar Event">
                    <Loading />
                </Dialog>
            );
        }
        return <Dialog title="Show Calendar Event">
            <EventView event={event} />
            <hr />
            <button type="button" className="btn btn-default"><GearIcon /> Edit...</button>
            <button type="button" className="btn btn-default"><TrashcanIcon /> Remove...</button>
            <hr />
            <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
        </Dialog>;
    }
}
