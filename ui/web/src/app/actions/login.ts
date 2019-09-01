import { loginService } from '../services/login'

export const actions = {
  SET_LOGIN_PENDING: 'SET_LOGIN_PENDING',
  SET_LOGIN_SUCCESS: 'SET_LOGIN_SUCCESS',
  SET_LOGIN_ERROR: 'SET_LOGIN_ERROR'
};

export function login(email:string, password:string) {
  return dispatch => {
    dispatch(setLoginPending(true));
    dispatch(setLoginSuccess(false));
    dispatch(setLoginError(null));

    loginService.login(email, password).then(
      token => {
        // eslint-disable-next-line @typescript-eslint/no-angle-bracket-type-assertion
        // @ts-ignore
        // eslint-disable-next-line @typescript-eslint/no-angle-bracket-type-assertion
        localStorage.setItem('token', <string>token["token"]);
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

function setLoginPending(isLoginPending) {
  return {
    type: actions.SET_LOGIN_PENDING,
    isLoginPending
  };
}

function setLoginSuccess(isLoginSuccess) {
  return {
    type: actions.SET_LOGIN_SUCCESS,
    isLoginSuccess
  };
}

function setLoginError(loginError) {
  return {
    type: actions.SET_LOGIN_ERROR,
    loginError
  }
}
