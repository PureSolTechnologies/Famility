import React from 'react';

import AdminMenu from './AdminMenu';
import Footer from './Footer';
import Header from './Header';

export default function AdminLayout( {children}) {
    return (
        <div className="container">
            <Header />
            <AdminMenu />
            {children}
            <Footer />
        </div>
    );
}
