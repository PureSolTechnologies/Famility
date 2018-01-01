import React from 'react';

import Tab from './Tab';

export default class TabComponent extends React.Component {

    static propTypes = {
        selected: React.PropTypes.string.isRequired,
    }

    constructor( props ) {
        super( props );
        this.state = { selected: this.props.selected };
    }

    select( name ) {
        this.setState( { selected: name });
    }

    getLinkClasses( name ) {
        if ( this.state.selected == name ) {
            return "nav-link active";
        } else {
            return "nav-link";
        }
    }

    render() {
        var tabHeaders = [];
        var tabs = [];
        for ( var id = 0; id < this.props.children.length; id++ ) {
            const child = this.props.children[id];
            if ( this.props.vertical ) {
                tabHeaders.push(
                    <a className={this.getLinkClasses( child.props.name )} onClick={() => this.select( child.props.name )}>
                        {child.props.heading}
                    </a>
                );
            } else {
                tabHeaders.push(
                    <li className="nav-item" key={id}>
                        <a className={this.getLinkClasses( child.props.name )} onClick={() => this.select( child.props.name )}>
                            {child.props.heading}
                        </a>
                    </li>
                );
            }
            tabs.push(
                <Tab key={child.props.name} tabId={child.props.name} selected={this.state.selected} heading={child.props.heading}>
                    {child.props.children}
                </Tab>
            );
        }
        if ( this.props.vertical ) {
            return (
                <div className="row">
                    <div className="md-col-2">
                        <nav className="nav flex-column">
                            {tabHeaders}
                        </nav>
                    </div>
                    <div className="md-col-10">
                        {tabs}
                    </div>
                </div>
            );
        } else {
            return (
                <div>
                    <ul className="nav nav-tabs">
                        {tabHeaders}
                    </ul>
                    {tabs}
                </div>
            );
        }
    }
}
