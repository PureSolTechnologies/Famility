import * as React from 'react';

export default class Collapse extends React.Component<any, undefined> {

    static propTypes = {
        id: React.PropTypes.string.isRequired,
        name: React.PropTypes.string.isRequired
    };

    constructor( props: any ) {
        super( props );
    }

    render() {
        return ( <div>
            <button className="btn btn-secondary" type="button" data-toggle="collapse" data-target={'#' + this.props.id} aria-expanded="false" aria-controls="collapseExample">
                {this.props.name}
            </button>
            <div className="collapse" id={this.props.id}>
                <div className="card card-block">
                    {this.props.children}
                </div>
            </div>
        </div> );
    }

}
