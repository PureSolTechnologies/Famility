import * as React from 'react';

const {PlusIcon, DashIcon, MentionIcon } = require( 'react-octicons' );

export default class Collapse extends React.Component<any, any> {

    static propTypes = {
        id: React.PropTypes.string.isRequired,
        name: React.PropTypes.string.isRequired
    };

    constructor( props: any ) {
        super( props );
        this.state = { shown: false };
        this.show = this.show.bind( this );
        this.hide = this.hide.bind( this );
    }

    private show() {
        this.setState( { shown: true });
    }

    private hide() {
        this.setState( { shown: false });
    }

    render() {
        return (
            <div>
                <button hidden={this.state.shown} className="btn btn-secondary" type="button" data-toggle="collapse" data-target={'#' + this.props.id} aria-expanded="false" aria-controls="collapseExample" onClick={this.show}>
                    <PlusIcon /> {this.props.name}
                </button>
                <button hidden={!this.state.shown} className="btn btn-secondary" type="button" data-toggle="collapse" data-target={'#' + this.props.id} aria-expanded="false" aria-controls="collapseExample" onClick={this.hide}>
                    <DashIcon />
                </button>
                <div className="collapse" id={this.props.id}>
                    <div className="card card-block">
                        {this.props.children}
                    </div>
                </div>
            </div>
        );
    }

}
