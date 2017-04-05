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

import DayCalendar from './pages/calendar/DayCalendar';
import WeekCalendar from './pages/calendar/WeekCalendar';
import MonthCalendar from './pages/calendar/MonthCalendar';
import YearCalendar from './pages/calendar/YearCalendar';
import Copyright from './pages/Copyright';
import LoginPage from './pages/LoginPage';
import MainDashboard from './pages/MainDashboard';
import MetricsDashboard from './pages/MetricsDashboard';
import PeopleAdministrator from './pages/PeopleAdministrator';
import Plugins from './pages/Plugins';
import SystemAdministrator from './pages/SystemAdministrator';

import CreateAppointment from './pages/dialog/calendar/CreateAppointment';
import AddUser from './pages/dialog/people/AddUser';

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
                <Route path="login(/:redirect)" component={LoginPage} />
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
            </Route>
            <Route path="/dialog" component={DialogLayout}>
                <Route path="calendar/create-appointment(/:date)(/:beginTime)(/:endTime)" component={CreateAppointment} />
                <Route path="people/add" component={AddUser} />
            </Route>
            <Route path="/admin" component={AdminLayout} onEnter={LoginController.requireAuth} >
                <Route path="people" component={PeopleAdministrator} />
                <Route path="system" component={SystemAdministrator} />
                <Route path="metrics" component={MetricsDashboard} />
            </Route>
        </Router>
    </Provider >,
    document.getElementById( 'app' )
);
