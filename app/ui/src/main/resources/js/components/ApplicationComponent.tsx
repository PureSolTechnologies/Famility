import * as React from 'react';
import { findDOMNode } from 'react-dom';

declare var $: any;

export default class ApplicationComponent<P, S> extends React.Component<P, S> {

    constructor( props: any ) {
        super( props );
    }

    /**
     * This method is called with a reference id to search for tooltip elements to enable them.
     */
    protected enableTooltip( referenceId: string ) {
        let component: any = findDOMNode( this.refs[referenceId] );
        $( component ).find( '[data-toggle="tooltip"]' ).tooltip();
    }

}
