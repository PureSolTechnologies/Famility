import React from 'react';
import ReactDOM from 'react-dom';

import { Router, Route, Redirect, browserHistory } from 'react-router';

import Layout from './Layout';

import Copyright from './pages/Copyright';
import MainDashboard from './pages/MainDashboard';
import Plugins from './pages/Plugins';

ReactDOM.render(
    <Router history={browserHistory}>
        <Redirect from="/" to="/home" />
        <Route path="/" component={Layout}>
            <Route path="home" component={MainDashboard} />
            <Route path="plugins" component={Plugins} />
            <Route path="copyright" component={Copyright} />
        </Route>
    </Router>,
    document.getElementById( 'app' )
);
