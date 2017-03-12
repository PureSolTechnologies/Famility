import React from 'react';

import Menu from './Menu';
import Footer from './Footer';

export default function Layout( {children}) {
    return (
        <div className="container">
            <Menu />
            {children}
            <Footer />
        </div>
    );
}
