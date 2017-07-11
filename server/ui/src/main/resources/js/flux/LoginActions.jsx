export const LOGIN_ACTION = 'LOGIN'
export const LOGOUT_ACTION = 'LOGOUT'

export function login(user) {
    return {
        type: LOGIN_ACTION,
        user: user
    };
}

export function logout() {
    return {
        type: LOGOUT_ACTION
    }    
}
