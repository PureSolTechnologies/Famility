import React from 'react';

import Tab from './Tab';

export default class TabComponent extends React.Component {

    constructor( props ) {
        super( props );
        this.state = { selected: 0 };
    }

    select( id ) {
        this.setState( { selected: id });
    }

    getLinkClasses( id ) {
        if ( this.state.selected == id ) {
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
            const currentId = id;
            if ( this.props.vertical ) {
                tabHeaders.push(
                    <a className={this.getLinkClasses( currentId )} onClick={() => this.select( currentId )}>
                        {child.props.heading}
                    </a>
                );
            } else {
                tabHeaders.push(
                    <li className="nav-item" key={id}>
                        <a className={this.getLinkClasses( currentId )} onClick={() => this.select( currentId )}>
                            {child.props.heading}
                        </a>
                    </li>
                );
            }
            tabs.push(
                <Tab key={currentId} tabId={currentId} selected={this.state.selected} heading={child.props.heading}>
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
