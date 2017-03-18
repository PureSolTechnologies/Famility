import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router, Route, Redirect, browserHistory } from 'react-router';

import store from './flux/Store';
import Layout from './Layout';
import AdminLayout from './AdminLayout';
import CalendarLayout from './CalendarLayout';

import LoginController from './controller/LoginController';

import AddUser from './pages/AddUser';
import DayCalendar from './pages/calendar/DayCalendar';
import WeekCalendar from './pages/calendar/WeekCalendar';
import MonthCalendar from './pages/calendar/MonthCalendar';
import YearCalendar from './pages/calendar/YearCalendar';
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
                <Redirect from="/calendar" to="/calendar/year" />
                <Route path="/calendar" component={CalendarLayout}>
                    <Route path="year(/:year)" component={YearCalendar} />
                    <Route path="month(/:year)(/:month)" component={MonthCalendar} />
                    <Route path="week(/:year)(/:week)" component={WeekCalendar} />
                    <Route path="day(/:year)(/:month)(/:day)" component={DayCalendar} />
                </Route>
            </Route>
            <Route path="/admin" component={AdminLayout} onEnter={LoginController.requireAuth}>
                <Route path="people" component={PeopleAdministrator} />
                <Route path="people/add" component={AddUser} />
                <Route path="system" component={SystemAdministrator} />
            </Route>
        </Router>
    </Provider>,
    document.getElementById( 'app' )
);
