import React from 'react';
import { Link } from 'react-router';

import PureSolTechnologies from './components/PureSolTechnologies';

export default function Footer() {
    return (
        <footer>
            <hr />
            <div className="row">
                <hr />
            </div>
            <div className="row">
                <div className="col-md-4">
                    <PureSolTechnologies />
                </div>
                <div className="col-md-4">
                </div>
                <div className="col-md-4">
                    <Link to="/copyright">Copyright</Link>
                </div>
            </div >

        </footer>
    );
}
