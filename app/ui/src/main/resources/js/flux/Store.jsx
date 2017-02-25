import { createStore, combineReducers } from 'redux';

import { LOGIN_ACTION, LOGOUT_ACTION } from './LoginActions';

function loginReducer( state = null, action ) {
    switch ( action.type ) {
        case LOGIN_ACTION:
            return action.user;
        case LOGOUT_ACTION:
            return null;
        default:
            return null;
    }
}

const store = createStore( combineReducers(
    { login: loginReducer }
) );

export default store;
