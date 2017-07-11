import React from 'react';

import Menu from './Menu';
import Footer from './Footer';

export default class DialogLayout extends React.Component {

    render() {
        return (
            <div>
                {this.props.children}
            </div>
        );
    }
}