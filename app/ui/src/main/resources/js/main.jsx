import React from 'react';
import ReactDOM from 'react-dom';

import { Router, Route, Redirect, browserHistory } from 'react-router';

import Layout from './Layout';
import AdminLayout from './AdminLayout';

import LoginController from './controllers/LoginController';

import Copyright from './pages/Copyright';
import MainDashboard from './pages/MainDashboard';
import Plugins from './pages/Plugins';
import PeopleAdministrator from './pages/PeopleAdministrator';
import SystemAdministrator from './pages/SystemAdministrator';

ReactDOM.render(
    <Router history={browserHistory}>
        <Redirect from="/" to="/home" />
        <Route path="/" component={Layout}>
            <Route path="home" component={MainDashboard} />
            <Route path="plugins" component={Plugins} />
            <Route path="copyright" component={Copyright} />
            <Route path="login/:redirect" component={LoginController} />
        </Route>
        <Route path="/admin" component={AdminLayout}>
            <Route path="people" component={PeopleAdministrator} onEnter={LoginController.requireAuth}/>
            <Route path="system" component={SystemAdministrator} onEnter={LoginController.requireAuth}/>
        </Route>
    </Router>,
    document.getElementById( 'app' )
);
