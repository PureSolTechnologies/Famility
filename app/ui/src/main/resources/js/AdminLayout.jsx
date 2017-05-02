import React from 'react';

import AdminMenu from './AdminMenu';
import Footer from './Footer';

export default function AdminLayout( {children}) {
    return (
        <div className="container">
            <AdminMenu />
            {children}
            <Footer />
        </div>
    );
}
