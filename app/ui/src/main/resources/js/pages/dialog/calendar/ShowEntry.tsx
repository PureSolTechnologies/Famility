import * as React from 'react';
import { browserHistory } from 'react-router';

const { TrashcanIcon, GearIcon  } = require( 'react-octicons' );

import CalendarController from '../../../controller/CalendarController';
import Dialog from '../../../components/dialog/Dialog';
import EventView from '../../../components/calendar/EventView';

export default class ShowEvent extends React.Component<any, any> {

    constructor( props: any ) {
        super( props );
        this.state = {
            entry: {}
        };
        this.close = this.close.bind( this );
    }

    componentDidMount(): void {
        var component = this;
        CalendarController.getEvent(
            this.props.params.id,
            function( entry ) {
                component.setState( {
                    entry: entry
                });
            },
            function( response ) { });
    }

    edit(): void {
        
    }

    remove(): void {
        
    }
    
    close() {
        var entry = this.state.entry;
        var referTo = this.props.location.query.origin;
        if ( referTo ) {
            this.props.router.push( referTo );
        } else {
            this.props.router.push( '/calendar/day/' + entry.date.year + '/' + entry.date.month + '/' + entry.date.dayOfMonth );
        }
    }

    render() {
        var entry = this.state.entry;
        if ( !entry.title ) {
            return <div />;
        }
        return <Dialog title="Show Calendar Event">
            <EventView entry={entry} />
            <hr />
            <button type="button" className="btn btn-default"><GearIcon /> Edit...</button>
            <button type="button" className="btn btn-default"><TrashcanIcon /> Remove...</button>
            <hr />
            <button type="button" className="btn btn-primary" onClick={this.close}>Close</button>
        </Dialog>;
    }
}
