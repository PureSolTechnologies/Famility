import * as React from 'react';
const { BellIcon, GiftIcon, TasklistIcon, SyncIcon, ClockIcon, QuestionIcon  } = require( 'react-octicons' );

import ApplicationComponent from '../ApplicationComponent';

/**
 * This is a small component to show a calendar entry in a short form.
 */
export default class EventIcon extends ApplicationComponent<any, undefined> {

    static propTypes = {
        type: React.PropTypes.string.isRequired
    };

    constructor( props: any ) {
        super( props );
    }

    public render() {
        switch ( this.props.type ) {
            case "reminder":
                return <BellIcon />;
            case "birthday":
                return <GiftIcon />;
            case "todo":
                return <TasklistIcon />;
            case "appointment":
                return <ClockIcon />;
            case "anniversary":
                return <SyncIcon />;
            default:
                return <QuestionIcon />;
        }
    }

}