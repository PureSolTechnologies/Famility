import React from 'react';
import ReactDOM from 'react-dom';
import { Provider } from 'react-redux';
import { Router, Route, Redirect, browserHistory } from 'react-router';

import store from './flux/Store';
import Layout from './Layout';
import AdminLayout from './AdminLayout';
import CalendarLayout from './CalendarLayout';
import DialogLayout from './DialogLayout';

import LoginController from './controller/LoginController';

import AccountsAdministrator from './pages/AccountsAdministrator';
import Contacts from './pages/Contacts';
import Copyright from './pages/Copyright';
import Currencies from './pages/Currencies';
import Finance from './pages/Finance';
import LoginPage from './pages/LoginPage';
import MainDashboard from './pages/MainDashboard';
import MetricsDashboard from './pages/MetricsDashboard';
import Plugins from './pages/Plugins';
import SystemAdministrator from './pages/SystemAdministrator';


import DayCalendar from './pages/calendar/DayCalendar';
import WeekCalendar from './pages/calendar/WeekCalendar';
import MonthCalendar from './pages/calendar/MonthCalendar';
import YearCalendar from './pages/calendar/YearCalendar';

import CreateAccount from './pages/dialog/accounts/CreateAccount';
import CreateEvent from './pages/dialog/calendar/CreateEvent';
import ShowEvent from './pages/dialog/calendar/ShowEvent';
import CreateContact from './pages/dialog/contacts/CreateContact';
import EditContact from './pages/dialog/contacts/EditContact';

function getCurrentYear() {
    var calendar = store.getState().calendar;
    return "/calendar/year/" + calendar.year;
}


function getCurrentMonth() {
    var calendar = store.getState().calendar;
    return "/calendar/month/" + calendar.year + "/" + calendar.month;
}

function getCurrentWeek() {
    var calendar = store.getState().calendar;
    return "/calendar/week/" + calendar.year + "/" + calendar.year;
}

function getCurrentDay() {
    var calendar = store.getState().calendar;
    return "/calendar/day/" + calendar.year + "/" + calendar.month + "/" + calendar.day;
}

ReactDOM.render(
    <Provider store={store}>
        <Router history={browserHistory}>
            <Redirect from="/" to="/home" />
            <Route path="/" component={Layout}>
                <Route path="home" component={MainDashboard} />
                <Route path="plugins" component={Plugins} />
                <Route path="copyright" component={Copyright} />
                <Route path="login" component={LoginPage} />
                <Route path="contacts" component={Contacts} />
                <Redirect from="/calendar" to="/calendar/year" />
                <Route path="/calendar" component={CalendarLayout}>
                    <Redirect from="/calendar/year" to={getCurrentYear()} />
                    <Redirect from="/calendar/month" to={getCurrentMonth()} />
                    <Redirect from="/calendar/week" to={getCurrentWeek()} />
                    <Redirect from="/calendar/day" to={getCurrentDay()} />
                    <Route path="year/:year" component={YearCalendar} />
                    <Route path="month/:year/:month" component={MonthCalendar} />
                    <Route path="week/:year/:week" component={WeekCalendar} />
                    <Route path="day/:year/:month/:day" component={DayCalendar} />
                </Route>
                <Route path="/finance" component={Finance} />
                <Route path="/finance/currencies" component={Currencies} />
            </Route>
            <Redirect from="/dialog" to="/" />
            <Route path="/dialog" component={DialogLayout}>
                <Route path="accounts/create" component={CreateAccount} />
                <Route path="calendar/create-event(/:date)(/:beginTime)(/:endTime)" component={CreateEvent} />
                <Route path="calendar/show-event/:id" component={ShowEvent} />
                <Route path="contacts/create" component={CreateContact} />
                <Route path="contacts/edit/:id" component={EditContact} />
            </Route>
            <Redirect from="/admin" to="/admin/accounts" />
            <Route path="/admin" component={AdminLayout} onEnter={LoginController.requireAuth} >
                <Route path="accounts" component={AccountsAdministrator} />
                <Route path="system" component={SystemAdministrator} />
                <Route path="metrics" component={MetricsDashboard} />
            </Route>
        </Router>
    </Provider >,
    document.getElementById( 'app' )
);
