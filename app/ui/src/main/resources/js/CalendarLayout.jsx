import React from 'react';

import Menu from './Menu';
import Footer from './Footer';

export default function CalendarLayout( {children}) {
    return (
        <div>
            <h1>Calendar</h1>
            {children}
        </div>
    );
}
