import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router, Route, Redirect, browserHistory } from 'react-router';

import store from './flux/Store';
import Layout from './Layout';
import AdminLayout from './AdminLayout';

import LoginController from './controller/LoginController';

import Copyright from './pages/Copyright';
import LoginPage from './pages/LoginPage';
import MainDashboard from './pages/MainDashboard';
import PeopleAdministrator from './pages/PeopleAdministrator';
import Plugins from './pages/Plugins';
import SystemAdministrator from './pages/SystemAdministrator';

ReactDOM.render(
    <Provider store={store}>
        <Router history={browserHistory}>
            <Redirect from="/" to="/home" />
            <Route path="/" component={Layout}>
                <Route path="home" component={MainDashboard} />
                <Route path="plugins" component={Plugins} />
                <Route path="copyright" component={Copyright} />
                <Route path="login(/:redirect)" component={LoginPage} />
            </Route>
            <Route path="/admin" component={AdminLayout}>
                <Route path="people" component={PeopleAdministrator} onEnter={LoginController.requireAuth} />
                <Route path="system" component={SystemAdministrator} onEnter={LoginController.requireAuth} />
            </Route>
        </Router>
    </Provider>,
    document.getElementById( 'app' )
);
