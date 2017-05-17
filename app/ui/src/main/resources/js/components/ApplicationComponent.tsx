import * as React from 'react';
import { findDOMNode } from 'react-dom';

declare var $: any;

export default class ApplicationComponent<P, S> extends React.Component<P, S> {

    private rootReference: string = '';

    constructor( props: P ) {
        super( props );
    }

    protected enableTooltips( componentRootReference: string ) {
        this.rootReference = componentRootReference;
    }

    /**
     * This method is called with a reference id to search for tooltip elements to enable them.
     */
    private enableTooltip( referenceId: string ) {
        let component: any = findDOMNode( this.refs[referenceId] );
        $( component ).find( '[data-toggle="tooltip"]' ).tooltip();
    }

    /**
     * This method is called with a reference id to search for tooltip elements to enable them.
     */
    private hideTooltip( referenceId: string ) {
        let component: any = findDOMNode( this.refs[referenceId] );
        $( component ).find( '[data-toggle="tooltip"]' ).tooltip( 'dispose' );
    }

    protected componentDidUpdate(): void {
        this.enableTooltip( this.rootReference );
    }

    protected componentWillUnmount(): void {
        this.hideTooltip( this.rootReference );
    }

}
