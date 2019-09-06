import {LoginService} from '../../services/LoginService'

export const actions = {
  SET_LOGIN_PENDING: 'SET_LOGIN_PENDING',
  SET_LOGIN_SUCCESS: 'SET_LOGIN_SUCCESS',
  SET_LOGIN_ERROR: 'SET_LOGIN_ERROR',
  LOGOUT: 'LOGOUT'
};

export function loginAction(email: string, password: string): any {
  return dispatch => {
    dispatch(setLoginPending(true));
    dispatch(setLoginSuccess(false));
    dispatch(setLoginError(null));

    LoginService.login(email, password).then(
      token => {
        let jwtDecode: any = require('jwt-decode');
        let decoded = jwtDecode(token["token"]);
        // console.log("decoded: " + JSON.stringify(decoded));

        localStorage.setItem('token', token["token"]);
        dispatch(setLoginPending(false));
        dispatch(setLoginSuccess(true));
      },
      error => {
        dispatch(setLoginPending(false));
        dispatch(setLoginError(error));
      }
    );
  }
}

function setLoginPending(isLoginPending): any {
  return {
    type: actions.SET_LOGIN_PENDING,
    isLoginPending
  };
}

function setLoginSuccess(isLoginSuccess): any {
  return {
    type: actions.SET_LOGIN_SUCCESS,
    isLoginSuccess
  };
}

function setLoginError(loginError): any {
  return {
    type: actions.SET_LOGIN_ERROR,
    loginError
  }
}

function setLogout(): any {
  return {
    type: actions.LOGOUT
  }
}

export function logoutAction() {
  return dispatch => {
    localStorage.removeItem('token');
    dispatch(setLogout());
  }
}
